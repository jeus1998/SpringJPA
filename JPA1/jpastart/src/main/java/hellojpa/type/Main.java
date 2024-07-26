package hellojpa.type;

public class Main {
    public static void main(String[] args) {
        Integer a = Integer.valueOf(10); // 새로운 객체 힙에 저장
        Integer b = a;
        System.out.println(a == b); // true
        System.out.println(b);

        b = Integer.valueOf(20); // 새로운 객체를 힙에 저장
        System.out.println(a == b); // false
        System.out.println(a);
        System.out.println(b);

        System.out.println("============");

        int c = 10; // 각각 스택 변수
        int d = c;

        System.out.println(c);
        System.out.println(d);

        d = 20;

        System.out.println(c);
        System.out.println(d);
    }
}
