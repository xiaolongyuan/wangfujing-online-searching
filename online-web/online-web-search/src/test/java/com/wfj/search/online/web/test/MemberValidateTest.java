package com.wfj.search.online.web.test;

import com.wfj.member.sdk.client.MemberClient;
import com.wfj.member.sdk.common.Config;
import com.wfj.member.sdk.common.MsgReturnDto;
import org.junit.*;

/**
 * <p>create at 15-12-24</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class MemberValidateTest {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Ignore
    public void test() throws Exception {
        Config.setPrivateKey("/home/liufl/unison-syncs/work/job/wfj/search/projects/idea_projects/wfj-search/online/" +
                "online-web/online-web-search/src/main/resources-development/" +
                "member_privateKey.dat");
        Config.setDomainName("search.wangfujing.com");
//        Config.setMemberUrl("http://192.168.2.226:8090/member-core/interface/");
        Config.setMemberUrl("http://10.6.2.17:8090/member-core/interface/");
        MsgReturnDto msgReturnDto = MemberClient.valLoginStatus("0b89efe30b171-8d9e-45d9-8258-f98aac3490d7");
        System.out.println(msgReturnDto.getCode());
        System.out.println(msgReturnDto.getDesc());
        System.out.println(msgReturnDto.getObject());
    }
}
