package com.wfj.search.online.index.miner;

import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * <br/>create at 15-11-11
 *
 * @author liuxh
 * @since 1.0.0
 */
@Ignore
public class StringSplitTest {
    private static final Logger logger = LoggerFactory.getLogger(StringSplitTest.class);

    @Test
    @Ignore
    public void Test() {
        String text = "DisMax查询解析器设计的初衷是处理用户输入的简单短语（没有复杂语法），在多个根据不同含义使用不同权重的字段间进行搜索。另外还有额外的选项，使用户可以根据具体用例（根据用户的输入）影响打分。\n" +
                "总的来说，DisMax查询解析器比标准Solr查询请求更像Google的接口。对于许多付费应用，这种相似性使DisMax成为更加适当的查询解析器。她能够接收简单的语法，却很少发生错误。";
        String[] strArray = text.split("[\\p{P}\\p{S}[a-z0-9A-Z]\\s+\\n\\t]");
        for (String str : strArray) {
            logger.info(str);
        }
    }

    @Test
    @Ignore
    public void testRead() {
        InputStream in = ClassLoader.getSystemResourceAsStream(
                "file/A Brief History Of Time.txt");
        try (BufferedReader bufferReader = new BufferedReader(new InputStreamReader(in))) {
            String tempString;
            while ((tempString = bufferReader.readLine()) != null) {
                if (StringUtils.isBlank(tempString.trim())) {
                    continue;
                }
                String[] array = tempString.split("[\\p{P}\\p{S}[a-z0-9A-Z]\\s+\\n\\t]");
                for (String s : array) {
                    if (StringUtils.isBlank(s)) {
                        continue;
                    }
                    logger.debug(s);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWrite() throws IOException {
        String filePath = this.getClass().getResource("").getPath() + "out-no-one-character-word.txt";
        logger.debug(filePath);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
        writer.write("1");
        writer.newLine();
        writer.write("2");
        writer.flush();
        writer.close();
    }
}
