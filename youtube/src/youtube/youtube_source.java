package youtube;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

public class youtube_source {

	String uurll;
	String html_page_sourece;

	public youtube_source(String url) {
		this.uurll = url;
	}

	/*
	 * خب، با استفاده از این تابع میایم و سورس اچ تی ام ال صفحه ی یوتوب که
	 * ویدیوی مورد نظر ما توش پخش میشه رو بدست میاریم و میریزیمش داخل یک استرینگ
	 * یا رشته. کدش زیاد سخت نیست. یه نکاه بندازید میفهمید چی به چیه
	 */
	public String getUrlSource() {
		StringBuilder sb = new StringBuilder();
		try {

			URL urll = new URL(uurll);
			URLConnection urlConn = urll.openConnection();
			urlConn.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
			InputStream inputStream = urlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "UTF-8");
			BufferedReader in = new BufferedReader(inputStreamReader);
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
			}
			in.close();
			html_page_sourece = sb.toString();

		} catch (Exception e) {

			System.out.println(e.getMessage());
			System.out.println(e.toString());
		}
		return html_page_sourece;
	}

	/*
	 * خب. با این تابع میایم و لیست تمام پروکسی هایی که رو سیستم هستن رو شناسایی
	 * میکنیم. برای کارکردن این برنامه لازمه که حتما فیلتر شکن داشته باشید برای
	 * ایکه بدون فیلتر شکن ، برنامه نمیتونه سورس صفحات یوتوب رو بخونه دقت کنید،
	 * اولین خط بعد از ترای داره میگه که برنامه به طور اتوماتیک از تنظیمات
	 * پروکسی فعال در سیستم عامل استفاده کنه. البته ما لازم نیست که لیست تمام
	 * پروکسی های سیستم عامل رو بدتس بیازیم . همون خط اول ست سیستم پراپرتی رو
	 * اجرا کنید کفایت میکنه
	 */
	public String detect_system_proxies() {

		StringBuilder sb = new StringBuilder();

		try {
			System.setProperty("java.net.useSystemProxies", "true");

			List<Proxy> l = ProxySelector.getDefault().select(
					new URI("http://www.youtube.com"));

			for (Iterator<Proxy> iter = l.iterator(); iter.hasNext();) {
				Proxy proxy = iter.next();
				InetSocketAddress addr = (InetSocketAddress) proxy.address();

				if (addr == null) {

					System.out.println("No Proxy");

				} else {
					sb.append("proxy hostname : " + addr.getHostName() + "\r\n");
					sb.append("proxy hostname : " + "proxy port : "
							+ addr.getPort() + "\r\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public String extract_scrip_section_from_youtube() {

		StringBuilder sb = new StringBuilder();
		String source = getUrlSource();
		Pattern p = Pattern.compile("var ytplayer.*?;</script>");
		Matcher m = p.matcher(source);
		m.find();
		sb.append(m.group(0));
		return sb.toString();
	}

	/*
	 * خب. تقریبا میتونم بگم که اصلی ترین متد کلاس ما همینه. من اول یه توضیحی
	 * درباره صفحات یوبتوب بدم وببینیم که ادرس یو ار ال ویدیویی که داره پخش
	 * میشه در کجا قرار داره. من این اطلاعات رو با مهندسی معکوس بدست اوردم. اگر
	 * رو صفحه یوتوب کلیک راست کنید و گزیه ی ویو پیج سورس رو بزنید میتونید سورس
	 * صفحه رو ببینید. داخل اون سورس یک تگ وجود داره به نام  
	 * Script
	 *  که به این صورت هست
	 *<script>var ytplayer = ytplayer || {};ytplayer.config
	 * ادرس یو ار ال مستقیم ویدیو های یوتوب در داخل این تگ قرار داره. اگه داخل تگ رو خوب
	 * نگاه کنید یه قسمتی داره به این نام 
	 * url_encoded_fmt_stream_map 
	 * لینک تمام ویدیو ها داخل این قرار دارن. من با استفاده از دوتا رجکس اکسپرشن اومدم و
	 * لینک ویدیو هارو استخراج کردم
	 */
	public String extract_urls() {

		StringBuilder sb = new StringBuilder();

		String s = extract_scrip_section_from_youtube();

		Pattern p = Pattern.compile("\"url_encoded_fmt_stream_map\".*?\",");
		Matcher m = p.matcher(s);
		m.find();
		String urls = m.group(0);

		Pattern p2 = Pattern.compile("http.*?u0026");
		Matcher m2 = p2.matcher(urls);
		while (m2.find()) {

			sb.append(m2.group().replace("\\u0026", "") + "\r\n");
		}
		return sb.toString();
	}

	
	//ادرس عکس ویدیوی در حال پخش 
	//هم در داخل تگ زیر قرار داره 
	//که من با یک رجکس اکسپرشن
	// استخراش کردم
	public Image get_pic() {


		Pattern p = Pattern.compile("<link itemprop=\"thumbnailUrl\" href.*?>");
		Matcher m = p.matcher(html_page_sourece);
		m.find();
		String pic_url = m.group(0);

		Pattern p2 = Pattern.compile("http.*?\"");
		Matcher m2 = p2.matcher(pic_url);
		m2.find();
		String pic_url2 = m2.group(0);

		pic_url2 = pic_url2.replace("\"", "");

		Image image = null;
		try {
			URL url = new URL(pic_url2);
			image = ImageIO.read(url);
		} catch (IOException e) {

			System.out.println(e.getMessage());
		}
		return image;
	}
}
