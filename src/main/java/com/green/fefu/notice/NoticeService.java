package com.green.fefu.notice;

import com.green.fefu.notice.model.*;

import java.util.List;
import java.util.Map;

public interface NoticeService {
    int postNotice(PostNoticeReq p);
    Map<String, List<GetNoticeRes>> getNotice(GetNoticeReq p);
    Map<String, GetNoticeRes> getNoticeLatest(GetNoticeReq p);

    int putNotice(PutNoticeReq p);

    int deleteNotice(DeleteNoticeReq p);
}
