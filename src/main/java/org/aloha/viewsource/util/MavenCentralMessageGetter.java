package org.aloha.viewsource.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by aloha on 2017/7/5.
 * 
 * @author aloha
 *
 */
public class MavenCentralMessageGetter {

	public static final String MVN_HOST = "34.226.89.199:80";

	public static final String BASE_URL = "http://" + MVN_HOST + "/";

	public static void main(String[] args) {
		SearchResult sr = getSearchedResultByPage("java", 3);
		if (sr != null) {
			sr.urls.forEach(e -> {
				System.out.println(e);
			});
			System.out.println(sr.urls.size());
		}
	}

	public static SearchResult getSearchedResultByPage(String query, int page) {
		String url = BASE_URL + "search?q=" + query;
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
			String resultString = doc.select("#maincontent>h2>b").text();
			SearchResult sr = new SearchResult();
			if (resultString != null) {
				int result = Integer.parseInt(resultString);
				sr.count = result;
			} else {
				return null;
			}

			sr.pageCount = doc.select("#maincontent>ul>li").size() - 2;

			if (page > sr.pageCount || page < 1) {
				throw new IllegalArgumentException("page index illegal");
			}
			if (page == 1) {
				Elements eles = doc.select("#maincontent>.im>a");
				if (eles != null) {
					sr.urls.addAll(eles.stream().map(e -> e.attr("abs:href")).collect(Collectors.toList()));
				}
				return sr;
			}
			doc = Jsoup.connect(url + "&p=" + page).get();
			Elements eles = doc.select("#maincontent>.im>a");
			if (eles != null) {
				sr.urls.addAll(eles.stream().map(e -> e.attr("abs:href")).collect(Collectors.toList()));
			}
			return sr;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static SearchResult getAllSearchedResult(String query) {
		String url = BASE_URL + "search?q=" + query;
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
			String resultString = doc.select("#maincontent>h2>b").text();
			SearchResult sr = new SearchResult();
			if (resultString != null) {
				int result = Integer.parseInt(resultString);
				sr.count = result;
			} else {
				return null;
			}

			sr.pageCount = doc.select("#maincontent>ul>li").size() - 2;

			Elements eles = doc.select("#maincontent>.im>a");
			if (eles != null) {
				sr.urls.addAll(eles.stream().map(e -> e.attr("abs:href")).collect(Collectors.toList()));
			}
			for (int page = 2; page <= sr.pageCount; ++page) {
				doc = Jsoup.connect(url + "&p=" + page).get();
				eles = doc.select("#maincontent>.im>a");
				if (eles != null) {
					sr.urls.addAll(eles.stream().map(e -> e.attr("abs:href")).collect(Collectors.toList()));
				}
			}
			return sr;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static class SearchResult {
		public int count;
		public int pageCount;
		public List<String> urls = new ArrayList<>();
	}

	public static List<String> getAllReleaseVersions(String url) {
		Elements eles = getElementsBySelector(url, ".grid.versions tbody tr td:eq(0) a");
		if (eles != null) {
			return eles.stream().map(e -> e.attr("abs:href")).collect(Collectors.toList());
		}
		return null;
	}

	public static String getPomString(String url) {
		Elements eles = getElementsBySelector(url, "#maincontent #snippets #maven-a");
		if (eles != null) {
			return eles.text();
		}
		return null;
	}

	/**
	 * get lastest project pom
	 * 
	 * @param projectUrl
	 * @return
	 */
	public static String getProjectLastestPom(String projectUrl) {
		List<String> versions = getAllReleaseVersions(projectUrl);
		if (versions == null) {
			return null;
		}
		return getPomString(versions.get(0));
	}

	/**
	 * get elements by selector and url
	 * 
	 * @param url
	 * @param selector
	 * @return
	 */
	public static Elements getElementsBySelector(String url, String selector) {
		try {
			Document doc = Jsoup.connect(url).get();
			return doc.select(selector);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
