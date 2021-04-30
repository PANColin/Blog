package com.blog.lucene;

import com.blog.entity.Blog;
import com.blog.util.DateUtil;
import com.blog.util.StringUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.StringReader;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class BlogIndex {
    private Directory dir = null;

    private IndexWriter getWriter()
            throws Exception {
        this.dir = FSDirectory.open(Paths.get("C://lucene", new String[0]));
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(this.dir, iwc);
        return writer;
    }

    public void addIndex(Blog blog)
            throws Exception {
        IndexWriter writer = getWriter();
        Document doc = new Document();
        doc.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
        doc.add(new TextField("title", blog.getTitle(), Field.Store.YES));
        doc.add(new StringField("releaseDate", DateUtil.formatDate(new Date(), "yyyy-MM-dd"), Field.Store.YES));
        doc.add(new TextField("content", blog.getContentNoTag(), Field.Store.YES));
        doc.add(new TextField("keyWord", blog.getKeyWord(), Field.Store.YES));
        writer.addDocument(doc);
        writer.close();
    }

    public void updateIndex(Blog blog)
            throws Exception {
        IndexWriter writer = getWriter();
        Document doc = new Document();
        //StringField 表示不分词 N Y Y
        doc.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
        doc.add(new TextField("title", blog.getTitle(), Field.Store.YES));
        doc.add(new StringField("releaseDate", DateUtil.formatDate(new Date(), "yyyy-MM-dd"), Field.Store.YES));
        doc.add(new TextField("content", blog.getContentNoTag(), Field.Store.YES));
        doc.add(new StringField("keyWord", blog.getKeyWord(), Field.Store.YES));
        writer.updateDocument(new Term("id", String.valueOf(blog.getId())), doc);
        writer.close();
    }

    public void deleteIndex(String blogId)
            throws Exception {
        IndexWriter writer = getWriter();
        writer.deleteDocuments(new Term[]{new Term("id", blogId)});
        writer.forceMergeDeletes();
        writer.commit();
        writer.close();
    }

    public List<Blog> searchBlog(String q)
            throws Exception {
        this.dir = FSDirectory.open(Paths.get("C://lucene", new String[0]));
        IndexReader reader = DirectoryReader.open(this.dir);
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        //创建布尔查询对象(使用组合查询)，可以数字和文本查询一起使用
        //BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
        String[] fields = {"title", "content", "keyWord"};
        //使用多个域进行查询,可以设置权重
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields, analyzer);
        Query query = queryParser.parse(q);
        //最多返回100条数据
        //TopDocs hits = indexSearcher.search(booleanQuery.build(), 100);
        TopDocs hits = indexSearcher.search(query, 100);
        //高亮搜索字
        QueryScorer scorer = new QueryScorer(query);
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
        highlighter.setTextFragmenter(fragmenter);

        List<Blog> blogList = new LinkedList();
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = indexSearcher.doc(scoreDoc.doc);
            Blog blog = new Blog();
            blog.setId(Integer.valueOf(Integer.parseInt(doc.get("id"))));
            blog.setReleaseDateStr(doc.get("releaseDate"));
            String title = doc.get("title");
            String content = StringEscapeUtils.escapeHtml(doc.get("content"));
            String keyWord = doc.get("keyWord");
            if (title != null) {
                TokenStream tokenStream = analyzer.tokenStream("title", new StringReader(title));
                String hTitle = highlighter.getBestFragment(tokenStream, title);
                if (StringUtil.isEmpty(hTitle)) {
                    blog.setTitle(title);
                } else {
                    blog.setTitle(hTitle);
                }
            }
            if (content != null) {
                TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(content));
                String hContent = highlighter.getBestFragment(tokenStream, content);
                if (StringUtil.isEmpty(hContent)) {
                    if (content.length() <= 200) {
                        blog.setContent(content);
                    } else {
                        blog.setContent(content.substring(0, 200));
                    }
                } else {
                    blog.setContent(hContent);
                }
            }
            if (keyWord != null) {
                TokenStream tokenStream = analyzer.tokenStream("keyWord", new StringReader(keyWord));
                String hKeyWord = highlighter.getBestFragment(tokenStream, keyWord);
                if (StringUtil.isEmpty(hKeyWord)) {
                    blog.setKeyWord(keyWord);
                } else {
                    blog.setKeyWord(hKeyWord);
                }
            }
            blogList.add(blog);
        }
        return blogList;
    }
}
