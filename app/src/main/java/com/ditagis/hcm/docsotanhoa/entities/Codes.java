package com.ditagis.hcm.docsotanhoa.entities;

/**
 * Created by ThanLe on 12/6/2017.
 */

public class Codes {
    private static Code_Describle[] codeDescribles;

    private Codes() {
        codeDescribles = new Code_Describle[]{new Code_Describle("40", " Bình thường"),
                new Code_Describle("41", " Ghi chỉ số ra ngoài"),
                new Code_Describle("42", " Báo chỉ số qua điện thoại"),
                new Code_Describle("54", " Ghi sai, ghi lố, nhập liệu sai"),
                new Code_Describle("55", " Giải trình code 5 kỳ trước"),
                new Code_Describle("56", " Giải trình code 6 kỳ trước\nKỳ này đọc được"),
                new Code_Describle("58", " Giải trình code 8 kỳ trước"),
                new Code_Describle("5F", " Giải trình code F kỳ trước"),
                new Code_Describle("5M", " Giải trình code M kỳ trước"),
                new Code_Describle("5Q", " Giải trình code Q kỳ trước"),
                new Code_Describle("5K", " Giải trình code K kỳ trước"),
                new Code_Describle("5N", " Giải trình code N kỳ trước"),
                new Code_Describle("60", " Ngưng có nước, nước yếu"),
                new Code_Describle("61", " Kiếng mờ, dơ, nứt"),
                new Code_Describle("62", " Kẹt số, lệch số, tuôn số\nquay ngược, gắn ngược"),
                new Code_Describle("63", " Bể kiếng, mất mặt số"),
                new Code_Describle("64", " Chủ gỡ, ống ngang\nnâng, hạ, hầm sâu"),
                new Code_Describle("66", " Lấp mất, không tìm thấy"),
                new Code_Describle("80", " ĐHN đã thay nhỏ hơn 7 ngày"),
                new Code_Describle("81", " Kỳ trước ĐHN ngưng\nKỳ này thay mới"),
                new Code_Describle("82", " Thay thử, thay định kỳ"),
                new Code_Describle("83", " Thay đổi cỡ"),
                new Code_Describle("F1", " Nhà đóng cửa"),
                new Code_Describle("F2", " Hộp bảo vệ ĐHN bị kẹt khóa"),
                new Code_Describle("F3", " Chất đồ không dọn được"),
                new Code_Describle("F4", " Đám tang (tiệc), ngập nước\nKhách hàng không cho đọc số"),
                new Code_Describle("K", "  Nhà đóng cửa không ở"),
                new Code_Describle("M0", " Đọc số đúng tháng"),
                new Code_Describle("M1", " Đọc số sau 1 tháng"),
                new Code_Describle("M2", " Đọc số sau 2 tháng"),
                new Code_Describle("M3", " Đọc số sau 3 tháng"),
                new Code_Describle("N1", " Giữ chỉ số do kỳ trước tạm tính lố\nnhà ĐCƠ, CĐ, KK"),
                new Code_Describle("N2", " Giữ chỉ số do khách hàng ghi lố"),
                new Code_Describle("N3", " Giữ chỉ số do nhân viên ghi lố"),
                new Code_Describle("X ", " ĐHN 4 số retour một lần"),
                new Code_Describle("68", " Bị khóa nước niêm phong\nbị cắt ống bên ngoài"),
                new Code_Describle("Q ", " Không có nước hoàn toàn")
        };
    }

    private static final Codes instance = new Codes();

    public static final Codes getInstance() {
        return instance;
    }

    public Code_Describle[] getCodeDescribles() {
        return codeDescribles;

    }
}
