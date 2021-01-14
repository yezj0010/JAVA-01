import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * created by DengJin on 2021/1/8 14:29
 */
public class XlassClassLoader extends ClassLoader {
    //指定路径
    private String path ;

    public static void main(String[] args) throws Exception {
        //这个类xlass的路径,此处写的绝对路径
        testLoad("F:/projects/java/JAVA-01/Week_01/week01_1_homework2/Hello.xlass");
    }

    public static void testLoad(String classPath) throws Exception{
        XlassClassLoader myClassLoader = new XlassClassLoader(classPath);
        //类的全称
        String packageNamePath = "Hello";
        //加载类
        Class<?> objCls = myClassLoader.findClass(packageNamePath);

        System.out.println("类加载器是:" + objCls.getClassLoader());

        Object object = objCls.newInstance();
        //通过反射方法
        Method infoMethod = objCls.getDeclaredMethod("hello");
        infoMethod.invoke(object);
    }

    /**
     * 重写findClass方法
     * @param name 是我们这个类的全路径
     * @return
     */
    @Override
    protected Class<?> findClass(String name) {
        Class info = null;
        try{
            // 获取该class文件字节码数组
            byte[] classData = getData();
            classData = changeBytes(classData);//遍历byte数组，每个255-当前值

            if (classData != null) {
                // 将class的字节码数组转换成Class类的实例
                info = defineClass(name, classData, 0, classData.length);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 遍历byte数组，每个字节=255-自身，得到新字节
     * @param bytes
     * @return
     */
    private byte[] changeBytes(byte[] bytes){
        byte[] newBytes = new byte[bytes.length];
        for(int i=0;i<bytes.length;i++){
            newBytes[i] = (byte)(255 - bytes[i]);
        }
        return newBytes;
    }

    /**
     * 将class文件转化为字节码数组
     * @return
     */
    private byte[] getData() {
        File file = new File(path);
        byte[] bytes = null;
        if (file.exists()){
            FileInputStream in = null;
            ByteArrayOutputStream out = null;
            try {
                in = new FileInputStream(file);
                out = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = in.read(buffer)) != -1) {
                    out.write(buffer, 0, size);
                }
                bytes = out.toByteArray();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            System.out.println("未找到文件"+path);
        }
        return bytes;
    }

    public XlassClassLoader(String classPath){
        path=classPath;
    }
}
