import java.io.*;
/**
 * 只复制字段
 * @author noah
 *
 */
public class AttributeReplace_2UnderCase {


	public static void main(String[] args) throws Exception{
		String file = "/Users/noah/project/db-smart-migrate-server/src/test/java/d.txt";
		//DBAtrr2__(file);
		DBAtrr2JAVA(file);
	}

	private  static void DBAtrr2JAVA(String file) throws FileNotFoundException, IOException {
		File file2 = new File(file);
		FileReader fis = new FileReader(file2);
		BufferedReader bufferedReader = new BufferedReader(fis);

		String line = "";
		String conetent = "";
		while ((line = bufferedReader.readLine()) != null) {// 如果之前文件为空，则不执 行输出
			if (line.trim().length() > 0) {
				String prefix = "";
				if (line.contains("char")) {
					prefix = " String ";
				} else if (line.contains("bigint")) {
					prefix = " Long ";
				} else if (line.contains("text")) {
					prefix = " String ";
				} else if (line.contains("datetime")) {
					prefix = " Date ";
				} else if (line.contains("int")) {
					prefix = " Integer ";
				} else if (line.contains("timestamp")) {
					prefix = " Date ";
				} else {
					prefix = "String ";
				}

				conetent += "private" + prefix
						+ line.substring(line.indexOf("`") + 1, line.lastIndexOf("`")).toLowerCase() + ";\r\n";
			}
		}
		fis.close();
		bufferedReader.close();

		conetent = conetent.replaceAll("_a", "A").replaceAll("_b", "B").replaceAll("_c", "C").replaceAll("_d", "D")
				.replaceAll("_e", "E").replaceAll("_f", "F").replaceAll("_g", "G").replaceAll("_h", "H")
				.replaceAll("_i", "I").replaceAll("_j", "J").replaceAll("_k", "K").replaceAll("_l", "L")
				.replaceAll("_m", "M").replaceAll("_n", "N").replaceAll("_o", "O").replaceAll("_p", "P")
				.replaceAll("_q", "Q").replaceAll("_r", "R").replaceAll("_s", "S").replaceAll("_t", "T")
				.replaceAll("_u", "U").replaceAll("_v", "V").replaceAll("_w", "W").replaceAll("_x", "X")
				.replaceAll("_y", "Y").replaceAll("_z", "X");

		FileWriter fw = new FileWriter(file2);
		fw.write(conetent);
		fw.flush();
		fw.close();
	}
	
	private   void DBAtrr2__(String file) throws FileNotFoundException, IOException {
		File file2 = new File(file);
		FileReader fis = new FileReader(file2);
		BufferedReader bufferedReader = new BufferedReader(fis);

		String line = "";
		String conetent = "";
		while ((line = bufferedReader.readLine()) != null) {// 如果之前文件为空，则不执 行输出
			if (line.trim().length() > 0) {
				String[] split = line.split("\\,");
				for (String string : split) {
					if(string != null) {
						conetent +=  "," + "'"+string.trim()+"'";
					}
					
				}
			}
		}
		fis.close();
		bufferedReader.close();

		FileWriter fw = new FileWriter(file2);
		fw.write(conetent.replaceFirst("\\,", ""));
		fw.flush();
		fw.close();
	}





}
