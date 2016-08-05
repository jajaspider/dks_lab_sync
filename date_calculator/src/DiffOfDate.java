
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author 915-hoyoung
 */
public class DiffOfDate {

    /**
     *
     * @param args
     * @throws java.io.IOException
     * @throws java.text.ParseException
     *
     */
    public static void main(String[] args) throws IOException, ParseException {

        //           파일 최신 수정 날짜 확인 후 예전 수정 날짜 파일 지우기
        String first = "c:/file_example/aaa.txt";
        String second = "c:/file_example/zzxc.txt";

        File firstFile = new File(first);
        File secondFile = new File(second);
        if (firstFile.exists() && secondFile.exists()) {
            SimpleDateFormat sf = new SimpleDateFormat("yy. MM. dd HH:mm:ss");

            Date beginDate = sf.parse(sf.format(firstFile.lastModified()));
            Date endDate = sf.parse(sf.format(secondFile.lastModified()));

            long diff = endDate.getTime() - beginDate.getTime();
            //long diffDays = diff / (24 * 60 * 60 * 1000);

            System.out.println("이름 : " + firstFile.getName());
            System.out.println("이름 : " + secondFile.getName());
            System.out.println("경로 : " + firstFile.getPath());
            System.out.println("경로 : " + secondFile.getPath());
            System.out.println("파일 여부 : " + firstFile.isFile());
            System.out.println("파일 여부 : " + secondFile.isFile());
            System.out.println(firstFile + " 파일의 수정 날짜 : " + sf.format(firstFile.lastModified()));
            System.out.println(secondFile + " 파일의 수정 날짜 : " + sf.format(secondFile.lastModified()));
            //System.out.println(diffDays);
            System.out.println(diff);

            if (diff > 0) {
                System.out.println(second + " 파일이 최신입니다.");
                firstFile.delete();
                System.out.println(first + " 파일 삭제 완료.");

            } else {
                System.out.println(first + " 파일이 최신입니다.");
                secondFile.delete();
                System.out.println(second + " 파일 삭제 완료.");
            }
        } else {
            //                        파일 이동
            File file = new File("c:/file_example/aaa.txt");
            File fileToMove = new File("c:/file_example/example1/aaa.txt");

            boolean isMoved = file.renameTo(fileToMove);
        }

    }

}
