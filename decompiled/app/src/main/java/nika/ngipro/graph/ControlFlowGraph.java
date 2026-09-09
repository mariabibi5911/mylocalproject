package nika.ngipro.graph;

import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OffsetInstruction;
import org.jf.dexlib2.iface.instruction.SwitchInstruction;

import java.util.*;

/**
 * Control Flow Graph Generator for NGI PRO 2.0
 * Generates CFG representation from DEX methods
 */
public class ControlFlowGraph {
    
    public static class BasicBlock {
        public int startAddress;
        public int endAddress;
        public List<String> instructions = new ArrayList<>();
        public List<Integer> successors = new ArrayList<>();
        public List<Integer> predecessors = new ArrayList<>();
        public String blockType = "NORMAL"; // NORMAL, CONDITIONAL, LOOP, SWITCH
        
        public BasicBlock(int start) {
            this.startAddress = start;
        }
        
        public void addInstruction(String instr) {
            instructions.add(instr);
        }
        
        public boolean containsAddress(int addr) {
            return addr >= startAddress && addr <= endAddress;
        }
    }
    
    public static class CFGResult {
        public Map<Integer, BasicBlock> blocks = new LinkedHashMap<>();
        public List<String> edges = new ArrayList<>();
        public int entryBlock;
        public List<Integer> exitBlocks = new ArrayList<>();
        
        public String toDotFormat(String methodName) {
            StringBuilder sb = new StringBuilder();
            sb.append("digraph ").append(methodName.replace("<", "_").replace(">", "_")).append(" {\n");
            sb.append("  rankdir=TB;\n");
            sb.append("  node [shape=box, style=filled, fillcolor=lightblue];\n\n");
            
            // Add nodes
            for (Map.Entry<Integer, BasicBlock> entry : blocks.entrySet()) {
                BasicBlock block = entry.getValue();
                sb.append("  block_").append(block.startAddress)
                  .append(" [label=\"").append(block.startAddress).append(": \\n");
                
                // Limit instructions shown for readability
                int maxShow = Math.min(block.instructions.size(), 8);
                for (int i = 0; i < maxShow; i++) {
                    String instr = block.instructions.get(i).replace("\"", "\\\"");
                    if (instr.length() > 60) instr = instr.substring(0, 57) + "...";
                    sb.append(instr).append("\\n");
                }
                if (block.instructions.size() > maxShow) {
                    sb.append("... (").append(block.instructions.size() - maxShow).append(" more)\\n");
                }
                
                sb.append("\", fillcolor=");
                switch (block.blockType) {
                    case "CONDITIONAL": sb.append("yellow"); break;
                    case "LOOP": sb.append("orange"); break;
                    case "SWITCH": sb.append("pink"); break;
                    default: sb.append("lightblue");
                }
                sb.append("];\n");
            }
            
            // Add edges
            for (String edge : edges) {
                sb.append("  ").append(edge).append(";\n");
            }
            
            sb.append("}\n");
            return sb.toString();
        }
        
        public String toJsonFormat() {
            StringBuilder sb = new StringBuilder();
            sb.append("{\n  \"method\": \"generated\",\n  \"blocks\": [\n");
            
            int count = 0;
            for (BasicBlock block : blocks.values()) {
                if (count > 0) sb.append(",\n");
                sb.append("    {\n");
                sb.append("      \"start\": ").append(block.startAddress).append(",\n");
                sb.append("      \"end\": ").append(block.endAddress).append(",\n");
                sb.append("      \"type\": \"").append(block.blockType).append("\",\n");
                sb.append("      \"instructions\": ").append(block.instructions.size()).append(",\n");
                sb.append("      \"successors\": ").append(block.successors).append("\n");
                sb.append("    }");
                count++;
            }
            
            sb.append("\n  ],\n  \"edges\": ").append(edges).append("\n}");
            return sb.toString();
        }
    }
    
    /**
     * Generate CFG from a method
     */
    public static CFGResult generateCFG(Method method) {
        CFGResult result = new CFGResult();
        
        try {
            List<? extends Instruction> instructions = method.getImplementation().getInstructions();
            if (instructions.isEmpty()) {
                return result;
            }
            
            // First pass: identify branch targets and block boundaries
            Set<Integer> blockStarts = new HashSet<>();
            Map<Integer, List<Integer>> branchTargets = new HashMap<>();
            
            blockStarts.add(0); // Entry block
            
            int address = 0;
            for (Instruction instr : instructions) {
                if (instr instanceof OffsetInstruction) {
                    OffsetInstruction offsetInstr = (OffsetInstruction) instr;
                    int target = address + offsetInstr.getCodeOffset();
                    blockStarts.add(target);
                    
                    branchTargets.computeIfAbsent(address, k -> new ArrayList<>()).add(target);
                }
                
                if (instr instanceof SwitchInstruction) {
                    SwitchInstruction switchInstr = (SwitchInstruction) instr;
                    for (int offset : switchInstr.getTargets()) {
                        int target = address + offset;
                        blockStarts.add(target);
                        branchTargets.computeIfAbsent(address, k -> new ArrayList<>()).add(target);
                    }
                }
                
                // Next instruction starts a new block after certain instructions
                String opcode = instr.getOpcode().name;
                if (opcode.contains("RETURN") || opcode.contains("GOTO") || 
                    opcode.contains("THROW") || opcode.contains("SWITCH")) {
                    int nextAddr = address + instr.getCodeUnits();
                    if (nextAddr < instructions.size() * 2) { // Approximate check
                        blockStarts.add(nextAddr);
                    }
                }
                
                address += instr.getCodeUnits();
            }
            
            // Second pass: build basic blocks
            List<Integer> sortedStarts = new ArrayList<>(blockStarts);
            Collections.sort(sortedStarts);
            
            Map<Integer, Integer> addressToBlock = new HashMap<>();
            for (int i = 0; i < sortedStarts.size(); i++) {
                int start = sortedStarts.get(i);
                int end = (i < sortedStarts.size() - 1) ? sortedStarts.get(i + 1) - 1 : address;
                
                BasicBlock block = new BasicBlock(start);
                block.endAddress = end;
                result.blocks.put(start, block);
                addressToBlock.put(start, i);
            }
            
            // Third pass: populate instructions and determine block types
            address = 0;
            BasicBlock currentBlock = null;
            
            for (Instruction instr : instructions) {
                // Find the block for this address
                int blockStart = 0;
                for (int start : sortedStarts) {
                    if (start <= address) {
                        blockStart = start;
                    } else {
                        break;
                    }
                }
                
                currentBlock = result.blocks.get(blockStart);
                if (currentBlock != null) {
                    currentBlock.addInstruction(formatInstruction(instr, address));
                    
                    String opcode = instr.getOpcode().name;
                    if (opcode.contains("IF")) {
                        currentBlock.blockType = "CONDITIONAL";
                    } else if (opcode.contains("SWITCH")) {
                        currentBlock.blockType = "SWITCH";
                    }
                }
                
                address += instr.getCodeUnits();
            }
            
            // Set end addresses properly
            for (Map.Entry<Integer, BasicBlock> entry : result.blocks.entrySet()) {
                BasicBlock block = entry.getValue();
                if (!block.instructions.isEmpty()) {
                    // Estimate end address based on instruction count
                    block.endAddress = block.startAddress + (block.instructions.size() * 2);
                }
            }
            
            // Fourth pass: build edges and detect loops
            Set<String> addedEdges = new HashSet<>();
            for (Map.Entry<Integer, BasicBlock> entry : result.blocks.entrySet()) {
                BasicBlock block = entry.getValue();
                int blockIndex = addressToBlock.get(entry.getKey());
                
                if (block.instructions.isEmpty()) continue;
                
                String lastInstr = block.instructions.get(block.instructions.size() - 1);
                
                // Check for conditional branches
                if (block.blockType.equals("CONDITIONAL")) {
                    // Add edge to fall-through block
                    if (blockIndex < sortedStarts.size() - 1) {
                        int nextStart = sortedStarts.get(blockIndex + 1);
                        BasicBlock nextBlock = result.blocks.get(nextStart);
                        if (nextBlock != null) {
                            block.successors.add(nextStart);
                            nextBlock.predecessors.add(block.startAddress);
                            String edge = "block_" + block.startAddress + " -> block_" + nextStart;
                            if (addedEdges.add(edge)) {
                                result.edges.add(edge);
                            }
                        }
                    }
                    
                    // Add edge to branch target
                    if (branchTargets.containsKey(block.startAddress)) {
                        for (int target : branchTargets.get(block.startAddress)) {
                            BasicBlock targetBlock = findBlockByAddress(result.blocks, target);
                            if (targetBlock != null) {
                                block.successors.add(targetBlock.startAddress);
                                targetBlock.predecessors.add(block.startAddress);
                                String edge = "block_" + block.startAddress + " -> block_" + targetBlock.startAddress;
                                if (addedEdges.add(edge)) {
                                    result.edges.add(edge);
                                }
                            }
                        }
                    }
                } else if (lastInstr.contains("GOTO")) {
                    // Unconditional jump
                    if (branchTargets.containsKey(block.startAddress)) {
                        for (int target : branchTargets.get(block.startAddress)) {
                            BasicBlock targetBlock = findBlockByAddress(result.blocks, target);
                            if (targetBlock != null) {
                                block.successors.add(targetBlock.startAddress);
                                targetBlock.predecessors.add(block.startAddress);
                                String edge = "block_" + block.startAddress + " -> block_" + targetBlock.startAddress;
                                if (addedEdges.add(edge)) {
                                    result.edges.add(edge);
                                }
                            }
                        }
                    }
                } else if (!lastInstr.contains("RETURN") && !lastInstr.contains("THROW")) {
                    // Fall-through to next block
                    if (blockIndex < sortedStarts.size() - 1) {
                        int nextStart = sortedStarts.get(blockIndex + 1);
                        BasicBlock nextBlock = result.blocks.get(nextStart);
                        if (nextBlock != null) {
                            block.successors.add(nextStart);
                            nextBlock.predecessors.add(block.startAddress);
                            String edge = "block_" + block.startAddress + " -> block_" + nextStart;
                            if (addedEdges.add(edge)) {
                                result.edges.add(edge);
                            }
                        }
                    }
                }
            }
            
            // Detect loops (back edges)
            for (Map.Entry<Integer, BasicBlock> entry : result.blocks.entrySet()) {
                BasicBlock block = entry.getValue();
                for (int succ : block.successors) {
                    if (succ <= block.startAddress) {
                        block.blockType = "LOOP";
                        break;
                    }
                }
            }
            
            // Set entry and exit blocks
            if (!sortedStarts.isEmpty()) {
                result.entryBlock = sortedStarts.get(0);
            }
            
            for (BasicBlock block : result.blocks.values()) {
                if (block.successors.isEmpty()) {
                    result.exitBlocks.add(block.startAddress);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    private static BasicBlock findBlockByAddress(Map<Integer, BasicBlock> blocks, int address) {
        // Find the block that contains or starts at this address
        for (BasicBlock block : blocks.values()) {
            if (block.startAddress == address) {
                return block;
            }
        }
        return null;
    }
    
    private static String formatInstruction(Instruction instr, int address) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%04X: ", address));
        sb.append(instr.getOpcode().name);
        
        // Add simple representation of operands
        if (instr instanceof org.jf.dexlib2.iface.instruction.ReferenceInstruction) {
            org.jf.dexlib2.iface.instruction.ReferenceInstruction ref = 
                (org.jf.dexlib2.iface.instruction.ReferenceInstruction) instr;
            sb.append(" ").append(ref.getReference().toString());
        } else if (instr instanceof org.jf.dexlib2.iface.instruction.LiteralInstruction) {
            org.jf.dexlib2.iface.instruction.LiteralInstruction lit = 
                (org.jf.dexlib2.iface.instruction.LiteralInstruction) instr;
            sb.append(" ").append(lit.getLiteral());
        }
        
        return sb.toString();
    }
    
    /**
     * Analyze CFG complexity
     */
    public static class ComplexityMetrics {
        public int cyclomaticComplexity;
        public int numBlocks;
        public int numEdges;
        public int numLoops;
        public int maxDepth;
        public String complexityLevel; // LOW, MEDIUM, HIGH, VERY_HIGH
        
        public String getSummary() {
            return String.format("Blocks: %d, Edges: %d, Loops: %d, Cyclomatic: %d [%s]",
                    numBlocks, numEdges, numLoops, cyclomaticComplexity, complexityLevel);
        }
    }
    
    public static ComplexityMetrics analyzeComplexity(CFGResult cfg) {
        ComplexityMetrics metrics = new ComplexityMetrics();
        metrics.numBlocks = cfg.blocks.size();
        metrics.numEdges = cfg.edges.size();
        
        // Count loops
        for (BasicBlock block : cfg.blocks.values()) {
            if (block.blockType.equals("LOOP")) {
                metrics.numLoops++;
            }
        }
        
        // Calculate cyclomatic complexity: E - N + 2P
        // Where E = edges, N = nodes, P = connected components (usually 1)
        metrics.cyclomaticComplexity = Math.max(1, metrics.numEdges - metrics.numBlocks + 2);
        
        // Determine complexity level
        if (metrics.cyclomaticComplexity <= 10) {
            metrics.complexityLevel = "LOW";
        } else if (metrics.cyclomaticComplexity <= 20) {
            metrics.complexityLevel = "MEDIUM";
        } else if (metrics.cyclomaticComplexity <= 50) {
            metrics.complexityLevel = "HIGH";
        } else {
            metrics.complexityLevel = "VERY_HIGH";
        }
        
        metrics.maxDepth = estimateMaxDepth(cfg);
        
        return metrics;
    }
    
    private static int estimateMaxDepth(CFGResult cfg) {
        // Simple BFS to estimate maximum path length
        if (cfg.blocks.isEmpty()) return 0;
        
        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> depths = new HashMap<>();
        
        queue.add(cfg.entryBlock);
        depths.put(cfg.entryBlock, 1);
        
        int maxDepth = 1;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            BasicBlock block = cfg.blocks.get(current);
            
            if (block == null) continue;
            
            int currentDepth = depths.get(current);
            
            for (int succ : block.successors) {
                if (!depths.containsKey(succ) || depths.get(succ) < currentDepth + 1) {
                    depths.put(succ, currentDepth + 1);
                    maxDepth = Math.max(maxDepth, currentDepth + 1);
                    queue.add(succ);
                }
            }
        }
        
        return maxDepth;
    }
}
