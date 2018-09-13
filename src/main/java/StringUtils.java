public class StringUtils {


    static String getSimpleName(String s){
        if (s.contains(".")){
            return s.substring(s.lastIndexOf(".") + 1);
        }
        return s;
    }


    public static String getPackageName(String s){
        if (s.contains(".")){
            return s.substring(0, s.lastIndexOf("."));
        }
        return s;
    }


    static String getFistLowName(String s){
        StringBuilder sb = new StringBuilder(s);
        sb.setCharAt(0, String.valueOf(sb.charAt(0)).toLowerCase().charAt(0));
        return sb.toString();
    }


    public static String getJdbcName(String s){
        if (s == null || s.length() == 0){
            return s;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            Character c = s.charAt(i);
            if (Character.isUpperCase(c)){
                if (sb.length() == 0){
                    sb.append(Character.toLowerCase(c));
                }else {
                    sb.append("_").append(Character.toLowerCase(c));
                }
            }else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    static String getJavaName(String s){
        if (s == null || s.length() == 0){
            return s;
        }
        StringBuilder sb = new StringBuilder();
        boolean nextIsUpcase = false;
        for (int i = 0; i < s.length(); i++) {
            Character c = s.charAt(i);
            if (sb.length() == 0){
                sb.append(Character.toUpperCase(c));
                continue;
            }
            if ("_".equals(String.valueOf(c))){
                nextIsUpcase = true;
                continue;
            }
            if (nextIsUpcase){
                sb.append(Character.toUpperCase(c));
                nextIsUpcase = false;
            }else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
