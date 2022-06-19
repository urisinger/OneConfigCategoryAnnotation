package cc.polyfrost.oneconfig.internal.plugin.asm.tweakers;

import cc.polyfrost.oneconfig.internal.plugin.asm.ITransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.File;

public class VigilantTransformer implements ITransformer {

    @Override
    public String[] getClassName() {
        return new String[]{"gg.essential.vigilance.Vigilant"};
    }

    /**
     * If anything here is changed, edit the corresponding method in OneConfigMixinPlugin!
     */
    @Override
    public void transform(String transformedName, ClassNode node) {
        if (!node.interfaces.contains("cc/polyfrost/oneconfig/config/compatibility/vigilance/VigilantAccessor")) {
            node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "oneconfig$config", "Lcc/polyfrost/oneconfig/config/compatibility/vigilance/VigilanceConfig;", null, null));
            node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "oneconfig$file", Type.getDescriptor(File.class), null, null));

            node.interfaces.add("cc/polyfrost/oneconfig/config/compatibility/vigilance/VigilantAccessor");
            MethodNode methodNode = new MethodNode(Opcodes.ACC_PUBLIC, "getPropertyCollector", "()Lgg/essential/vigilance/data/PropertyCollector;", null, null);
            LabelNode labelNode = new LabelNode();
            methodNode.instructions.add(labelNode);
            methodNode.instructions.add(new LineNumberNode(421421, labelNode));
            methodNode.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            methodNode.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "gg/essential/vigilance/Vigilant", "propertyCollector", "Lgg/essential/vigilance/data/PropertyCollector;"));
            methodNode.instructions.add(new InsnNode(Opcodes.ARETURN));
            node.methods.add(methodNode);

            MethodNode methodNode2 = new MethodNode(Opcodes.ACC_PUBLIC, "handleOneConfigDependency", "(Lgg/essential/vigilance/data/PropertyData;Lgg/essential/vigilance/data/PropertyData;)V", null, null);
            LabelNode labelNode2 = new LabelNode();
            LabelNode labelNode3 = new LabelNode();
            LabelNode labelNode4 = new LabelNode();
            methodNode2.instructions.add(labelNode2);
            methodNode2.instructions.add(new LineNumberNode(15636436, labelNode2));
            methodNode2.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            methodNode2.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "gg/essential/vigilance/Vigilant", "oneconfig$config", "Lcc/polyfrost/oneconfig/config/compatibility/vigilance/VigilanceConfig;"));

            methodNode2.instructions.add(new JumpInsnNode(Opcodes.IFNULL, labelNode4));

            methodNode2.instructions.add(labelNode3);
            methodNode2.instructions.add(new LineNumberNode(15636437, labelNode3));
            methodNode2.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            methodNode2.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "gg/essential/vigilance/Vigilant", "oneconfig$config", "Lcc/polyfrost/oneconfig/config/compatibility/vigilance/VigilanceConfig;"));
            methodNode2.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
            methodNode2.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
            methodNode2.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/polyfrost/oneconfig/config/compatibility/vigilance/VigilanceConfig", "addDependency", "(Lgg/essential/vigilance/data/PropertyData;Lgg/essential/vigilance/data/PropertyData;)V", false));

            methodNode2.instructions.add(labelNode4);
            methodNode2.instructions.add(new LineNumberNode(15636438, labelNode4));
            methodNode2.instructions.add(new InsnNode(Opcodes.RETURN));
            node.methods.add(methodNode2);

            for (MethodNode method : node.methods) {
                if (method.name.equals("initialize")) {
                    InsnList list = new InsnList();
                    list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    list.add(new FieldInsnNode(Opcodes.GETFIELD, "gg/essential/vigilance/Vigilant", "oneconfig$file", Type.getDescriptor(File.class)));
                    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "cc/polyfrost/oneconfig/internal/plugin/hooks/VigilantHook", "returnNewConfig", "(Lgg/essential/vigilance/Vigilant;Ljava/io/File;)Lcc/polyfrost/oneconfig/config/compatibility/vigilance/VigilanceConfig;", false));
                    list.add(new FieldInsnNode(Opcodes.PUTFIELD, "gg/essential/vigilance/Vigilant", "oneconfig$config", "Lcc/polyfrost/oneconfig/config/compatibility/vigilance/VigilanceConfig;"));
                    method.instructions.insertBefore(method.instructions.getLast().getPrevious(), list);
                } else if (method.name.equals("addDependency") && method.desc.equals("(Lgg/essential/vigilance/data/PropertyData;Lgg/essential/vigilance/data/PropertyData;)V")) {
                    InsnList list = new InsnList();

                    list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    list.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "gg/essential/vigilance/Vigilant", "handleOneConfigDependency", "(Lgg/essential/vigilance/data/PropertyData;Lgg/essential/vigilance/data/PropertyData;)V", false));

                    method.instructions.insertBefore(method.instructions.getLast().getPrevious(), list);
                } else if (method.name.equals("<init>") && method.desc.equals("(Ljava/io/File;Ljava/lang/String;Lgg/essential/vigilance/data/PropertyCollector;Lgg/essential/vigilance/data/SortingBehavior;)V")) {
                    InsnList list = new InsnList();
                    list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    list.add(new FieldInsnNode(Opcodes.PUTFIELD, "gg/essential/vigilance/Vigilant", "oneconfig$file", Type.getDescriptor(File.class)));
                    method.instructions.insertBefore(method.instructions.getLast().getPrevious(), list);
                }
            }
        }
    }
}
