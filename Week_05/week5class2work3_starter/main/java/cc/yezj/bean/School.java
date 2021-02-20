package cc.yezj.bean;

import lombok.Data;

@Data
public class School implements ISchool {
    
    Klass klass;

    @Override
    public void ding(){
        System.out.println("---------------------------------------------------");
        System.out.println("Class1 has " + this.klass.getStudents().size()+"students");
        System.out.println("Class1 = " + this.klass);
        System.out.println("---------------------------------------------------");
    }
    
}
