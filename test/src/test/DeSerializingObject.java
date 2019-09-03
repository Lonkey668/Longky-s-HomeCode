package test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * 	反序列化Employee对象
 * @author Administrator
 *
 */
public class DeSerializingObject {

	public static void main(String[] args) {
		Employee employeeInput = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		try {
			fis = new FileInputStream("E://Employee.txt");
			ois = new ObjectInputStream(fis);
			employeeInput = (Employee)ois.readObject();
			
			System.out.println("已经把Employee.txt文件中的内容反序列化成Employee对象");
			fis.close();
			ois.close();
			
		}catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("name of employee is : "+employeeInput.getSerializeValueName());
		System.out.println("Salary of employee is : "+employeeInput.getNonSerializeValueSalary());
	}

}
