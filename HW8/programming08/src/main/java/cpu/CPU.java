package cpu;

import cpu.instr.all_instrs.InstrFactory;
import cpu.instr.all_instrs.Instruction;
import transformer.Transformer;

public class CPU {

    Transformer transformer = new Transformer();
    MMU mmu = MMU.getMMU();


    /**
     * execInstr specific numbers of instructions
     *
     * @param number numbers of instructions
     */
    public int execInstr(long number) {
        // 执行过的指令的总长度
        int totalLen = 0;
        while (number > 0) {
            totalLen+=execInstr();
            number--;
        }
        return totalLen;
    }

    /**
     * execInstr a single instruction according to eip value
     */
    private int execInstr() {
        String eip = CPU_State.eip.read();
        int len = decodeAndExecute(eip);
        CPU_State.eip.write(transformer.intToBinary(Integer.parseInt(eip,2)+len+""));
        return len;
    }

    private int decodeAndExecute(String eip) {
        int opcode = instrFetch(eip, 1);//只用返回一个字节长度的opcode，传入的只是eip寄存器中的地址
        Instruction instruction = InstrFactory.getInstr(opcode);//返回对应opcode的指令
        assert instruction != null;
        //exec the target instruction
        int len = instruction.exec(eip, opcode);//指令执行
        return len;
    }

    /**
     * @param eip
     * @param length opcode的字节数，本作业只使用单字节opcode
     * @return
     */
    private int instrFetch(String eip, int length) {
        // TODO X   FINISHED √
        String segReg = CPU_State.cs.read();
        char[] opcode = mmu.read(segReg+eip,length*8);
        return Integer.valueOf(transformer.binaryToInt(String.valueOf(opcode)));
    }
    //一直读指令直到遇到hlt
    public void execUntilHlt(){
        while (true){
            if (execInstr()==0){
                break;
            }
        }
    }

}
