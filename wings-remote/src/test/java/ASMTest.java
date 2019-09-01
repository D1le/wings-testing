import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by sp00x on 8/25/2019.
 * Project: wings-testing
 */
public class ASMTest {

    @Test
    void name() throws Exception {
//        getClassDeps(TestClassDependency2.class).forEach(System.out::println);
    }

    @Test
    void name2() throws Exception {
//        Action<String> a = () -> "dd";
//        getClassDeps(a.getClass()).forEach(System.out::println);
    }

    private Set<String> getClassDeps(Class<?> from) throws Exception {
        ClassReader reader = new ClassReader(from.getName());
        Set<String> out = new HashSet<String>();
        char[] charBuffer = new char[reader.getMaxStringLength()];
        for (int i = 1; i < reader.getItemCount(); i++) {
            int itemOffset = reader.getItem(i);
            if (itemOffset > 0 && reader.readByte(itemOffset - 1) == 7) {
                // A CONSTANT_Class entry, read the class descriptor
                String classDescriptor = reader.readUTF8(itemOffset, charBuffer);
                Type type = Type.getObjectType(classDescriptor);
                while (type.getSort() == Type.ARRAY) {
                    type = type.getElementType();
                }
                if (type.getSort() != Type.OBJECT) {
                    // A primitive type
                    continue;
                }
                String name = type.getClassName();
//                if (filter.isRelevant(name)) {
                out.add(name);
//                }
            }
        }
        return out;
    }
}
