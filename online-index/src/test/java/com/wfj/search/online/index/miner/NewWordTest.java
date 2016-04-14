package com.wfj.search.online.index.miner;

import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * <br/>create at 15-11-11
 *
 * @author liuxh
 * @since 1.0.0
 */
@Ignore
public class NewWordTest {
    private static final Logger logger = LoggerFactory.getLogger(NewWordTest.class);
    private int d = 5;
    private int frequency = 10;
    private int num = 2;// 2
    //    String fileName = "A Brief History Of Time.txt";
//    String resultFileName = "out1.txt";
//    String resultFileName = "out1-no-one-character-word.txt";
    String fileName = "xiyouji.txt";
    //    String resultFileName = "out2.txt";
    String resultFileName = "out2-no-one-character-word.txt";

    @Test
    public void test() throws IOException {
        Map<String, CandidateWord> agglomerationDegreeMap = new HashMap<>();
        long total = 0;
        Map<String, CandidateWord> map = new HashMap<>();
        List<String> strings = new ArrayList<>();
        InputStream in = new FileInputStream(
                this.getClass().getResource("/file").getPath() + File.separator + fileName);
        try (BufferedReader bufferReader = new BufferedReader(new InputStreamReader(in))) {
            String tempString;
            while ((tempString = bufferReader.readLine()) != null) {
                tempString = sbc2Dbc(tempString.trim());
                if (StringUtils.isBlank(tempString)) {
                    continue;
                }
                String[] array = tempString.split("[\\p{P}\\p{S}[a-z0-9A-Z]\\s+\\n\\t]");
                for (String s : array) {
                    if (StringUtils.isBlank(s)) {
                        continue;
                    }
                    strings.add(s);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String str : strings) {
            for (int i = 0; i < str.length(); i++) {
                for (int j = 0; j < d && j < str.length() - i; j++) {
                    String s = str.substring(i, i + j + 1);
                    total++;
                    CandidateWord word = map.get(s);
                    if (word == null) {
                        word = new CandidateWord();
                        word.frequency = 1L;
                        word.s = s;
                        map.put(s, word);
                    } else {
                        word.frequency += 1L;
                    }
                    String preS = str.substring(0, i);
                    if (StringUtils.isBlank(preS)) {
                        continue;
                    }
                    Map<String, Long> preMap = word.preMap;
                    for (int k = preS.length(); k > 0 && k > preS.length() - d; k--) {
                        String p = preS.substring(k - 1);
                        Long pl = preMap.get(p);
                        if (pl == null) {
                            pl = 1L;
                            preMap.put(p, pl);
                        } else {
                            preMap.put(p, pl + 1);
                        }
                        word.preSum += 1L;
                    }
                    String postS = str.substring(i + j + 1);
                    Map<String, Long> postMap = word.postMap;
                    for (int k = 0; k < postS.length() && k < d + 1; k++) {
                        String p = postS.substring(0, k + 1);
                        Long pl = postMap.get(p);
                        if (pl == null) {
                            pl = 1L;
                            postMap.put(p, pl);
                        } else {
                            postMap.put(p, pl + 1);
                        }
                        word.postSum += 1L;
                    }
                }
            }
        }
        Collection<CandidateWord> words = map.values();
        List<CandidateWord> list = new ArrayList<>(words.size());
        words.forEach(p -> {
            if (p.s.length() < num) {
                return;
            }
            if (p.frequency < frequency) {
                return;
            }
            Map<String, Long> preM = p.preMap;
            double preEntropy = 0;
            for (Map.Entry<String, Long> e : preM.entrySet()) {
                double ratio = e.getValue() / (double) p.preSum;
                preEntropy -= Math.log(ratio) * ratio;
            }
            p.preEntropy = preEntropy;
            Map<String, Long> postM = p.postMap;
            double postEntropy = 0;
            for (Map.Entry<String, Long> e : postM.entrySet()) {
                double ratio = e.getValue() / (double) p.postSum;
                postEntropy -= Math.log(ratio) * ratio;
            }
            p.postEntropy = postEntropy;
            list.add(p);
        });
        Collections.sort(list);
        logger.debug("排序后数据：");
        String filePath = this.getClass().getResource("/file").getPath() + File.separator + resultFileName;
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
        list.forEach(w -> {
            logger.debug("候选词：[{}]，出现频数：[{}]，左邻字信息熵：[{}]，右邻字信息熵：[{}]",
                    w.s, w.frequency, w.preEntropy, w.postEntropy);
            try {
                writer.write("候选词：[" + w.s + "]，出现频数：[" + w.frequency + "]，" +
                        "左邻字信息熵：[" + w.preEntropy + "]，右邻字信息熵：[" + w.postEntropy + "]");
                writer.newLine();
            } catch (IOException e) {
                logger.error("写数据出错", e);
            }
            agglomerationDegreeMap.put(w.s, w);
        });
        writer.flush();
        writer.close();

        logger.debug("总候选词数：{}", total);
        for (Map.Entry<String, CandidateWord> entry : agglomerationDegreeMap.entrySet()) {
        }
    }

    @Test
    @Ignore
    public void testPre() {
        Map<String, CandidateWord> map = new HashMap<>();
        String[] strArray = new String[]{"四是四十是十十四是十四四十是四十"};
        for (String str : strArray) {
            for (int i = 0; i < str.length(); i++) {
                for (int j = 0; j < d && j < str.length() - i; j++) {
                    String s = str.substring(i, i + j + 1);
                    CandidateWord word = map.get(s);
                    if (word == null) {
                        word = new CandidateWord();
                        word.frequency = 1L;
                        word.s = s;
                        map.put(s, word);
                    } else {
                        word.frequency += 1L;
                    }
                    String preS = str.substring(0, i);
                    if (StringUtils.isBlank(preS)) {
                        continue;
                    }
                    Map<String, Long> preMap = word.preMap;
                    for (int k = preS.length(); k > 0 && k > preS.length() - d; k--) {
                        String p = preS.substring(k - 1);
                        Long pl = preMap.get(p);
                        if (pl == null) {
                            pl = 1L;
                            preMap.put(p, pl);
                        } else {
                            preMap.put(p, pl + 1);
                        }
                        word.preSum += 1L;
                    }
                }
            }
        }
        Collection<CandidateWord> words = map.values();
        List<CandidateWord> list = new ArrayList<>(words.size());
        words.forEach(p -> {
            Map<String, Long> pm = p.preMap;
            double entropy = 0;
            for (Map.Entry<String, Long> e : pm.entrySet()) {
                double ratio = e.getValue() / (double) p.preSum;
                entropy -= Math.log(ratio) * ratio;
            }
            p.preEntropy = entropy;
            list.add(p);
        });
        Collections.sort(list);
        logger.debug("排序后数据：");
        list.forEach(w -> logger
                .debug("候选词：[{}]，出现频数：[{}]，左邻字数据：[{}]，左邻字信息熵：[{}]", w.s, w.frequency, w.preMap, w.preEntropy));
    }

    @Test
    @Ignore
    public void testPost() {
        Map<String, CandidateWord> map = new HashMap<>();
        String[] strArray = new String[]{"四是四十是十十四是十四四十是四十"};
        for (String str : strArray) {
            for (int i = 0; i < str.length(); i++) {
                for (int j = 0; j < d && j < str.length() - i; j++) {
                    String s = str.substring(i, i + j + 1);
                    CandidateWord word = map.get(s);
                    if (word == null) {
                        word = new CandidateWord();
                        word.frequency = 1L;
                        word.s = s;
                        map.put(s, word);
                    } else {
                        word.frequency += 1L;
                    }
                    String postS = str.substring(i + j + 1);
                    Map<String, Long> postMap = word.postMap;
                    for (int k = 0; k < postS.length() && k < d + 1; k++) {
                        String p = postS.substring(0, k + 1);
                        Long pl = postMap.get(p);
                        if (pl == null) {
                            pl = 1L;
                            postMap.put(p, pl);
                        } else {
                            postMap.put(p, pl + 1);
                        }
                        word.postSum += 1L;
                    }
                }
            }
        }
        Collection<CandidateWord> words = map.values();
        List<CandidateWord> list = new ArrayList<>(words.size());
        words.forEach(p -> {
            Map<String, Long> pm = p.postMap;
            double entropy = 0;
            for (Map.Entry<String, Long> e : pm.entrySet()) {
                double ratio = e.getValue() / (double) p.postSum;
                entropy -= Math.log(ratio) * ratio;
            }
            p.postEntropy = entropy;
            list.add(p);
        });
        Collections.sort(list);
        logger.debug("排序后数据：");
        list.forEach(w -> logger.debug("候选词：[{}]，出现频数：[{}]，右邻字信息熵：[{}]", w.s, w.frequency, w.postEntropy));
    }

    @Test
    @Ignore
    public void testEntropyOfInformation() {
        String str = "四是四十是十十四是十四四十是四十";
        Map<String, CandidateWord> map = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < d && j < str.length() - i; j++) {
                String s = str.substring(i, i + j + 1);
                CandidateWord word = map.get(s);
                if (word == null) {
                    word = new CandidateWord();
                    word.frequency = 1L;
                    word.s = s;
                    map.put(s, word);
                } else {
                    word.frequency += 1L;
                }
                String postS = str.substring(i + j + 1);
                Map<String, Long> postMap = word.postMap;
                for (int k = 0; k < postS.length() && k < d + 1; k++) {
                    String p = postS.substring(0, k + 1);
                    Long pl = postMap.get(p);
                    if (pl == null) {
                        pl = 1L;
                        postMap.put(p, pl);
                    } else {
                        postMap.put(p, pl + 1);
                    }
                    word.postSum += 1L;
                }
            }
        }
        Collection<CandidateWord> words = map.values();
        List<CandidateWord> list = new ArrayList<>(words.size());
        words.forEach(p -> {
            Map<String, Long> pm = p.postMap;
            double entropy = 0;
            for (Map.Entry<String, Long> e : pm.entrySet()) {
                double ratio = e.getValue() / (double) p.postSum;
                entropy -= Math.log(ratio) * ratio;
            }
            p.postEntropy = entropy;
            logger.debug("候选词：[{}]，出现频数：[{}]，右邻字信息熵：[{}]", p.s, p.frequency, p.postEntropy);
            list.add(p);
        });
        Collections.sort(list);
        logger.debug("排序后数据：");
        list.forEach(w -> logger.debug("候选词：[{}]，出现频数：[{}]，右邻字信息熵：[{}]", w.s, w.frequency, w.postEntropy));
    }

    @Test
    @Ignore
    public void testGetCandidateAndPost() {
        String str = "四是四十是十十四是十四四十是四十";
        Map<String, CandidateWord> map = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < d && j < str.length() - i; j++) {
                String s = str.substring(i, i + j + 1);
                CandidateWord word = map.get(s);
                if (word == null) {
                    word = new CandidateWord();
                    word.frequency = 1L;
                    map.put(s, word);
                } else {
                    word.frequency += 1L;
                }
                String postS = str.substring(i + j + 1);
                Map<String, Long> postMap = word.postMap;
                for (int k = 0; k < postS.length() && k < d + 1; k++) {
                    String p = postS.substring(0, k + 1);
                    Long pl = postMap.get(p);
                    if (pl == null) {
                        pl = 1L;
                        postMap.put(p, pl);
                    } else {
                        postMap.put(p, pl + 1);
                    }
                    word.postSum += 1L;
                }
            }
        }
        logger.debug("候选词：{}", map);
        Map<String, Long> postM = map.get("四").postMap;
        assertEquals(3L, postM.get("十").longValue());
        assertEquals(2L, postM.get("是").longValue());
        assertEquals(1L, postM.get("四").longValue());
        assertEquals(2L, postM.get("十是").longValue());
        assertEquals(1L, postM.get("是四").longValue());
        assertEquals(1L, postM.get("四十").longValue());
        assertEquals(1L, postM.get("是十").longValue());
        assertEquals(1L, postM.get("是四十").longValue());
    }

    @Test
    @Ignore
    public void testPostStr() {
        String str = "四是四十是十十四是十四四十是四十";
        Map<String, Long> map = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < d && j < str.length() - i; j++) {
                String s = str.substring(i, i + j + 1);
                String postS = str.substring(i + j + 1);
                Map<String, Long> postMap = new HashMap<>();
                for (int k = 0; k < postS.length() && k < d + 1; k++) {
                    String p = postS.substring(0, k + 1);
                    Long pl = postMap.get(p);
                    if (pl == null) {
                        pl = 1L;
                        postMap.put(p, pl);
                    } else {
                        postMap.put(p, pl + 1);
                    }
                }
                logger.info("{}, {}", s, postMap);
            }
        }
        logger.debug("候选词：{}", map);
    }

    @Test
    @Ignore
    public void testGetCandidateWords() {
        String str = "四是四十是十十四是十四四十是四十";
        Map<String, Long> map = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < d & j < str.length() - i; j++) {
                String s = str.substring(i, i + j + 1);
                logger.debug(s);
                Long l = map.get(s);
                if (l == null) {
                    map.put(s, 1L);
                } else {
                    map.put(s, l + 1L);
                }
            }
        }
        logger.debug("候选词：{}", map);
    }

    public static String sbc2Dbc(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);

            }
        }
        return new String(c);
    }

    class CandidateWord implements Comparable<CandidateWord> {
        String s;
        Long frequency = 0L;
        Map<String, Long> preMap = new HashMap<>();
        Map<String, Long> postMap = new HashMap<>();
        long preSum;
        long postSum;
        double preEntropy;
        double postEntropy;

        @Override
        public int compareTo(CandidateWord o) {
            if (o == null) {
                return -1;
            }
            // 先比价频数
            if (this.frequency < o.frequency) {
                return 1;
            } else if (this.frequency > o.frequency) {
                return -1;
            }
            // 再比较左邻字信息熵
            if (this.preEntropy < o.preEntropy) {
                return 1;
            } else if (this.preEntropy > o.preEntropy) {
                return -1;
            }
            // 再比较右邻字信息熵
            if (this.postEntropy < o.postEntropy) {
                return 1;
            } else if (this.postEntropy > o.postEntropy) {
                return -1;
            }
            return 0;
        }

        @Override
        public String toString() {
            return "CandidateWord{" +
                    "s='" + s + '\'' +
                    ", frequency=" + frequency +
                    ", preMap=" + preMap +
                    ", postMap=" + postMap +
                    ", preSum=" + preSum +
                    ", postSum=" + postSum +
                    ", preEntropy=" + preEntropy +
                    ", postEntropy=" + postEntropy +
                    '}';
        }
    }
}
