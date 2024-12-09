package processor.optimizer;

import middle.llvm_ir.*;
import middle.llvm_ir.function.IrFunction;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.instruction.IrPhiInstr;
import middle.llvm_ir.instruction.memory.IrAllocaInstr;
import middle.llvm_ir.instruction.memory.IrLoadInstr;
import middle.llvm_ir.instruction.memory.IrStoreInstr;
import middle.llvm_ir.utils.constant.IrConstInt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

public class Mem2Reg {
    private final IrModule module;
    // current allocaInstr
    private IrAllocaInstr curAllocaInstr = null;
    // the list of instructions(loadInstr) which use curAllocaInstr
    private ArrayList<IrInstruction> useCurInstrList = null;
    // the list of blocks(contains the loadInstr) which use curAllocaInstr
    private ArrayList<IrBasicBlock> useCurBlockList = null;
    // the list of instructions(storeInstr) which define curAllocaInstr
    private ArrayList<IrInstruction> defCurInstrList = null;
    // the list of blocks(contains the storeInstr) which define curAllocaInstr
    private ArrayList<IrBasicBlock> defCurBlockList = null;
    // for renaming
    private Stack<IrValue> stack = null;

    public Mem2Reg(IrModule module) {
        this.module = module;
    }

    public void run() {
        for (IrFunction function : module.getFuncList()) {
            for (IrBasicBlock block : function.getBlocks()) {
                // LinkedList -> ArrayList, traverse new container
                ArrayList<IrInstruction> instrList = new ArrayList<>(block.getInstrList());
                for (IrInstruction instruction : instrList) {
                    // allocaInstr is the starting definition
                    if (instruction instanceof IrAllocaInstr allocaInstr) {
                        // non array object
                        if (!allocaInstr.getTargetType().isArray()) {
                            initAttr(allocaInstr);

                            insertPhiInstr();

                            rename(function.getBlocks().get(0));
                        }
                    }
                }
            }
        }
    }

    private void initAttr(IrAllocaInstr allocaInstr) {
        curAllocaInstr = allocaInstr;
        useCurInstrList = new ArrayList<>();
        useCurBlockList = new ArrayList<>();
        defCurInstrList = new ArrayList<>();
        defCurBlockList = new ArrayList<>();
        stack = new Stack<>();

        for (IrUse use : allocaInstr.getUseList()) {
            IrUser user = use.getUser(); // the value which use curAllocaInstr

            if (user instanceof IrStoreInstr storeInstr && storeInstr.getParentBlock().alive()) { // define curAllocaInstr
                IrBasicBlock storeBlock = storeInstr.getParentBlock();

                defCurInstrList.add(storeInstr);
                if (!defCurBlockList.contains(storeBlock)) {
                    defCurBlockList.add(storeBlock);
                }
            }

            if (user instanceof IrLoadInstr loadInstr && loadInstr.getParentBlock().alive()) { // use curAllocaInstr
                IrBasicBlock loadBlock = loadInstr.getParentBlock();

                useCurInstrList.add(loadInstr);
                if (!useCurBlockList.contains(loadBlock)) {
                    useCurBlockList.add(loadBlock);
                }
            }
        }
    }

    private void insertPhiInstr() {
        HashSet<IrBasicBlock> F = new HashSet<>(); // blocks where ф is added
        Stack<IrBasicBlock> W = new Stack<>(); // blocks that contain definitions of curAllocaInstr

        for (IrBasicBlock block : defCurBlockList) {
            W.push(block);
        }

        // execute algorithm
        while (!W.isEmpty()) {
            IrBasicBlock X = W.pop();

            // the DF of define_allocaInstr
            for (IrBasicBlock Y : X.getDF()) {
                if (!F.contains(Y)) {
                    addPhi(Y);
                    F.add(Y);
                    if (!defCurBlockList.contains(Y)) {
                        W.push(Y);
                    }
                }
            }
        }
    }

    // add v ← φ(...) at entry of Y
    private void addPhi(IrBasicBlock block) {
        IrPhiInstr phiInstr = new IrPhiInstr(curAllocaInstr.getTargetType(), IrBuilder.getInstance().getLocalVarName(block.getParentFunc()));

        block.getInstrList().addFirst(phiInstr);
        phiInstr.setParentBlock(block);

        useCurInstrList.add(phiInstr);
        defCurInstrList.add(phiInstr);
    }

    // DFS to rename variable and delete alloca\store\load
    private void rename(IrBasicBlock entrance) {
        // record the push time of stack when traverse entrance
        int cnt = 0;

        // use iterator to traverse and modify the instrList
        Iterator<IrInstruction> iterator = entrance.getInstrList().iterator();
        while (iterator.hasNext()) {
            IrInstruction instruction = iterator.next();

            // delete current allocaInstr
            if (instruction.equals(curAllocaInstr)) {
                iterator.remove();
            }
            // push store value into stack
            else if (instruction instanceof IrStoreInstr storeInstr && defCurInstrList.contains(storeInstr)) {
                stack.push(storeInstr.getValue());
                cnt++;
                iterator.remove();
            }
            // push phi into stack
            else if (instruction instanceof IrPhiInstr phiInstr && defCurInstrList.contains(phiInstr)) {
                stack.push(phiInstr);
                cnt++;
            }
            // load value -> peek()
            else if (instruction instanceof IrLoadInstr loadInstr && useCurInstrList.contains(loadInstr)) {
                // stack.isEmpty -> unInit
                loadInstr.allUserChangeToNewValue(stack.isEmpty() ? new IrConstInt(loadInstr.getType(), 0) : stack.peek());
                iterator.remove();
            }
        }

        // traverse the nextBlocks to backfill phiInstr
        for (IrBasicBlock nextBlock : entrance.getNextBlocks()) {
            IrInstruction firstInstr = nextBlock.getFirstInstr();
            // judge is this phi or not
            if (firstInstr instanceof IrPhiInstr phiInstr && useCurInstrList.contains(phiInstr)) {
                // stack.isEmpty -> unInit
                phiInstr.addOption(entrance, stack.isEmpty() ? new IrConstInt(phiInstr.getType(), 0) : stack.peek());
            }
        }

        // DFS
        for (IrBasicBlock idomed : entrance.getIdomeds()) {
            rename(idomed);
        }

        // reset the stack
        for (int i = 0; i < cnt; i++) {
            stack.pop();
        }
    }
}
