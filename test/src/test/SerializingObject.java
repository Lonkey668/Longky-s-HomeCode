package test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * 	序列化Employee对象
 * @author Administrator
 *
 */
public class SerializingObject {

	public static void main(String[] args) {
		
		Employee employeeOutput = null;
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		employeeOutput = new Employee();
		employeeOutput.setSerializeValueName("序列化测试");
		employeeOutput.setNonSerializeValueSalary(5000);
		
		try {
			fos = new FileOutputStream("E://Employee.txt");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(employeeOutput);
			
			System.out.println("已经把employee对象序列化到Employee.txt文件中");
			oos.close();
			fos.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
