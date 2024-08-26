package com.green.fefu.chcommon;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class Parser {
    private static final int zoneCode = 0;
    private static final int addr = 1;
    private static final int grade = 0;
    private static final int uClass = 1;
    private static final int cNumber = 2;

    public static String[] addressParser(String address) {
        log.info("Parsing address: " + address);

        if (address == null || address.trim().isEmpty()) {
            log.info("값이 없습니다");
            return new String[]{"값이 없습니다", "값이 없습니다", "값이 없습니다"};
        }

        String[] result = address.split("#", 3);

        // 배열의 길이가 3이 되도록 값이 없으면 빈 문자열로 초기화
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i].trim();
        }
        if (result.length < 3) {
            String[] temp = new String[3];
            System.arraycopy(result, 0, temp, 0, result.length);
            for (int i = result.length; i < 3; i++) {
                temp[i] = "";
            }
            result = temp;
        }

        return result;
    }

    public static String addressParserMerge(String zoneCode, String addr, String detail) {
        log.info("zoneCode = {}, addr = {}, detail = {}", zoneCode, addr, detail);

        if ((zoneCode == null || zoneCode.trim().isEmpty()) &&
                (addr == null || addr.trim().isEmpty()) &&
                (detail == null || detail.trim().isEmpty())) {
            return null;
        }

        return String.format("%s # %s # %s",
                zoneCode != null ? zoneCode.trim() : "",
                addr != null ? addr.trim() : "",
                detail != null ? detail.trim() : "");
    }

    //    학년 반 합친거
    public static String classParser(String data) {
        log.info("classParser {}", data);
        if(data == null){
            return null ;
        }

        String[] result = classParserArray(data);

        if (!(result[2]==null)) {
            return String.format("%s %s %s", result[grade], result[uClass], result[cNumber]);
        }
        return String.format("%s %s", result[grade], result[uClass]);
    }

    //    학년 반 나눈거
    public static String[] classParserArray(String originData) {
        log.info("classParserArray data: {}", originData);
        String[] result = new String[3];
        String data = originData;
        if(data == null){
            return result ;
        }

        result[grade] = String.format("%s", data.substring(0, 1));

        result[uClass] = String.format("%d", Integer.parseInt(originData.substring(1, 3)));
        if (data.length() == 5) {
            result[cNumber] = String.format("%d", Integer.parseInt(originData.substring(3, 5)));
        }
        log.info("result = {}", Arrays.toString(result));
        return result;
    }

    public static String phoneParser(String data) {
        log.info("phoneParser data: {}", data);
        int lastHyphenIndex = data.lastIndexOf('-');
        return data.substring(lastHyphenIndex + 1);
    }
}
