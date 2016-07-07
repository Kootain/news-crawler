import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class test {
	public static void main(String[] args){
		String str1 = "http://news.163.com/16/0707/20/BRD9A3NT00014SEH.html";
		String str2 = "http://war.163.com/16/0702/09/BQV8MCDF00014OVF.html";
		Pattern timePattern = Pattern.compile("http://\\w+.163.com/(\\d+)/(\\d{2})(\\d{2})/(\\d+)/\\w+");
		Matcher m = timePattern.matcher(str2);
		if(m.find())
		System.out.println(m.group());
	}
}
