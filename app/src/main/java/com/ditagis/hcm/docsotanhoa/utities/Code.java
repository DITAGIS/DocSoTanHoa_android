package com.ditagis.hcm.docsotanhoa.utities;

/**
 * Created by ThanLe on 11/27/2017.
 */

public class Code {
    private static final Code instance = new Code();
    private static String[] codes;

    private Code() {
        codes = new String[]{"40 Bình thường", "41 Ghi chỉ số ra ngoài", "42 Báo chỉ số qua điện thoại",
                "54 Ghi sai, ghi lố, nhập liệu sai", "55 Giải trình code 5 kỳ trước",
                "56 Giải trình code 6 kỳ trước\nKỳ này đọc được", "58 Giải trình code 8 kỳ trước",
                "5F Giải trình code F kỳ trước", "5M Giải trình code M kỳ trước",
                "5Q Giải trình code Q kỳ trước", "5K Giải trình code K kỳ trước",
                "5N Giải trình code N kỳ trước",
                "60 Ngưng có nước, nước yếu", "61 Kiếng mờ, dơ, nứt", "62 Kẹt số, lệch số, tuôn số\nquay ngược, gắn ngược",
                "63 Bể kiếng, mất mặt số", "64 Chủ gỡ, ống ngang\nnâng, hạ, hầm sâu", "66 Lấp mất, không tìm thấy",
                "80 ĐHN đã thay nhỏ hơn 7 ngày", "81 Kỳ trước ĐHN ngưng\nKỳ này thay mới",
                "82 Thay thử, thay định kỳ", "83 Thay đổi cỡ",
                "F1 Nhà đóng cửa", "F2 Hộp bảo vệ ĐHN bị kẹt khóa", "F3 Chất đồ không dọn được", "F4 Đám tang (tiệc), ngập nước\nKhách hàng không cho đọc số",
                "K  Nhà đóng cửa không ở",
                "M0 Đọc số đúng tháng", "M1 Đọc số sau 1 tháng", "M2 Đọc số sau 2 tháng", "M3 Đọc số sau 3 tháng",
                "N1 Giữ chỉ số do kỳ trước tạm tính lố\nnhà ĐCƠ, CĐ, KK", "N2 Giữ chỉ số do khách hàng ghi lố", "N3 Giữ chỉ số do nhân viên ghi lố",
                "X  ĐHN 4 số retour một lần",
                "68 Bị khóa nước niêm phong\nbị cắt ống bên ngoài",
                "Q  Không có nước hoàn toàn"
        };
    }

    public static final Code getInstance() {
        return instance;
    }

    public String[] getCodes() {
        return codes;

    }
}
