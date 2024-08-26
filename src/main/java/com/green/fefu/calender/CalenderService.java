package com.green.fefu.calender;

import com.green.fefu.calender.repository.CalenderRepository;
import com.green.fefu.calender.req.CalenderReq;
import com.green.fefu.entity.Calender;
import com.green.fefu.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.green.fefu.exception.bch.BchErrorCode.QUERY_RESULT_ERROR;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalenderService {
    private final CalenderRepository calenderRepository;

    @Transactional
    public void postCalender(CalenderReq p) {
        Calender calender = new Calender();
        calender.setType(p.getCalenderType());
        calender.setName(p.getCalenderName());
        calender.setSchedule(Date.valueOf(p.getCalenderDate()));
        calenderRepository.save(calender);

        if (calender.getCalenderId() == null || calender.getCalenderId() == 0) {
            throw new CustomException(QUERY_RESULT_ERROR);
        }
    }

    public List<Map> getCalenderList() {
        List<Calender> result = calenderRepository.findAllOrderBySchedule();
        List<Map> calenderDtoList = new ArrayList<>();
        for (Calender calender : result) {
            Map map = new HashMap();
            map.put("CalenderId", calender.getCalenderId());
            map.put("AA_YMD", calender.getSchedule());
            map.put("STATE", calender.getType());
            map.put("EVENT_NM", calender.getName());
            calenderDtoList.add(map);
        }
        return calenderDtoList;
    }
}
