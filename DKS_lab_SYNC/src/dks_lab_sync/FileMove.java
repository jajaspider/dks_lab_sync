/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dks_lab_sync;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/*
구조
1. 왼쪽 오른쪽 파일 있을 경우

1-1 왼쪽 파일이 최신일 때
ㅇ 오른쪽 파일을 백업 폴더에 이동 후 왼쪽파일을 오른쪽 으로 이동
ㅇ 백업 폴더가 없을 경우 백업 폴더 생성 후 이동

1-2 오른쪽 파일이 최신일 때
ㅇ 왼쪽 파일을 백업 폴더에 이동
ㅇ 백업 폴더가 없을 경우 백업 폴더 생성 후 이동

2. 왼쪽엔 파일 있고 오른쪽엔 없을 경우
ㅇ 왼쪽 파일을 오른쪽으로 이동.

3. 왼쪽엔 파일 없고 오른쪽엔 있을 경우
ㅇ 그대로 둠.

사용할 File객체 메소드
1. renameTo() : 파일명 / 폴더명 변경.
2. get Parent() : 부모 경로에 대한 경로명을 문자열로 넘겨줌.
3. mkdirs() : 존재하지않는 부모 폴더까지 포함하여 해당 경로에 폴더 생성.
4. getName() : 파일이나 폴더의 이름을 넘겨줌.
5. lastModified() : 해당 경로 파일의 최종 수정 일자를 반환.
6. SimpleDateFormat : Date를 사용자가 지정한 형식에 맞게 출력 할 수 있도록 하는 클래스
7. SimpleDateFormat.parse() : 지정한 텍스트를 Date형식으로 바꿔줌.(Date 객체 생성)
8. getTime() : 설정된 시각을 밀리초로 변환해줌.
*/

/**
 *
 * @author HoYoung
 */
public class FileMove {

    /**
     * @param leftFile
     * @param rightFile
     */
    public static void file_Move(String leftFile, String rightFile) { //파일 이동 함수
        File l_file = new File(leftFile);
        File r_file = new File(rightFile);
        l_file.renameTo(r_file); //파일 이동
    }

    public static void left_backup(String leftFile, String rightFile) {
        File l_Path = new File(leftFile);
        File r_Path = new File(rightFile);

        String right_Path = r_Path.getParent(); //오른쪽 파일의 부모 경로
        String path = right_Path + "_SYNC_BACKUP" + "\\";
        //백업 폴더 만들 경로

        File f = new File(path);
        f.mkdirs(); //폴더 생성

        String fileName = l_Path.getName(); //왼쪽 파일의 이름만 가져오기

        File fToMove = new File(path + fileName);
        //백업 폴더의 경로 + 파일 이름

        l_Path.renameTo(fToMove); //백업 폴더로 이동

    }

    public static void right_backup(String leftFile, String rightFile) {
        File r_Path = new File(rightFile);

        String right_Path = r_Path.getParent(); //오른쪽 파일의 부모 경로

        String path = right_Path + "_SYNC_BACKUP" + "\\";
        //백업 폴더 만들 경로
        File f = new File(path);
        f.mkdirs(); //폴더 생성

        String rFileName = r_Path.getName(); //오른쪽 파일의 이름만 가져오기

        File r_fToMove = new File(path + rFileName);
        //백업 폴더의 경로 + 파일이름
        r_Path.renameTo(r_fToMove); //백업 폴더로 이동

        file_Move(leftFile, rightFile); //왼쪽 파일을 오른쪽으로 이동
    }

    public static void DiffofDate(String leftFile, String rightFile) throws ParseException {
        File left_f = new File(leftFile); //왼쪽 파일 객체 생성
        File right_f = new File(rightFile); //오른쪽 파일 객체 생성

        SimpleDateFormat sf = new SimpleDateFormat("yy. MM. dd HH:mm:ss"); //날짜 확인

        Date left_Date = sf.parse(sf.format(left_f.lastModified())); //날짜 계산 준비
        Date right_Date = sf.parse(sf.format(right_f.lastModified())); //날짜 계산 준비

        long diff = right_Date.getTime() - left_Date.getTime(); //날짜 계산

        System.out.println(left_f + " 왼쪽 파일 파일의 수정 날짜 : " + sf.format(left_f.lastModified()));
        System.out.println(right_f + " 오른쪽 파일 파일의 수정 날짜 : " + sf.format(right_f.lastModified()));

        if (diff > 0) { //오른쪽 파일이 최신 버전일 경우
            System.out.println(rightFile + " is the newest version.");
            left_backup(leftFile, rightFile); //왼쪽 파일 백업
        } else { //왼쪽 파일이 최신 버전일 경우
            System.out.println(leftFile + " is the newest version.");
            right_backup(leftFile, rightFile); //오른쪽 파일 백업
        }
    }

    /**
     *
     * @param args
     * @throws java.text.ParseException
     */
    public static void main(String[] args) throws ParseException {
        // TODO code application logic here
//////////////////////////////////////테스트용//////////////////////////////////////
        for (int i = 1; i <= 6; i++) {
            String first = "C:/test1/abc (" + i + ").txt";
            String second = "D:/fadams/test2/abc (" + i + ").txt";
            DiffofDate(first, second);
        }
//////////////////////////////////////테스트용//////////////////////////////////////

    }
}
