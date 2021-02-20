package cc.yezj;

import lombok.Data;

@Data
public class School implements ISchool {
    
    Klass klass;

    @Override
    public void ding(){
        System.out.println("Class1 have " + this.klass.getStudents().size());
        
    }
    
}
