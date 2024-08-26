package com.green.fefu.notice;

import com.green.fefu.common.model.ResultDto;
import com.green.fefu.notice.model.*;
import java.util.List;
import java.util.Map;

public interface NoticeController {

    ResultDto<Integer> postNotice(PostNoticeReq p);
    ResultDto<Map<String, List<GetNoticeRes>>> getNotice(GetNoticeReq p);

    ResultDto<Map<String, GetNoticeRes>> getNoticeLatest(GetNoticeReq p);

    ResultDto<Integer> putNotice(PutNoticeReq p);

    ResultDto<Integer> deleteNotice(DeleteNoticeReq p);

}
