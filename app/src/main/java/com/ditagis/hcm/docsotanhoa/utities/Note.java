package com.ditagis.hcm.docsotanhoa.utities;

/**
 * Created by ThanLe on 11/27/2017.
 */

public class Note {
    private static final Note instance = new Note();
    private static String[] notes;
    private static String[] notes_sub_dutchi, notes_sub_kinhdoanh;

    private Note() {
        notes = new String[]{"Khác",
                "Đứt chì",
                "Báo kinh doanh",
                "Báo hết kinh doanh",
                "Đấu chung với giếng",
                "Đấu chung với máy bơm",
                "Cập nhật giếng",
                "Cập nhật điện thoại",
                "Cập nhật vị trí",
                "Cập nhật số nhà tạm",
                "Đồng hồ âm sâu",
                "Đồng hồ kẹt tường, kẹt cửa",
                "Đồng hồ ngập nước",
                "Gãy tay van",
                "Hư hộp bảo vệ",
                "Mất nắp bảo vệ",
                "Hư khóa hộp bảo vệ",
                "Đồng hồ bị bể vành, biến dạng",
                "Ghi chú nghi gian lận",
                "Ghi chú báo thay lại",
                "Báo nước dơ",
                "Báo nước yếu",
                "Báo không nước",
                "Báo chưa tái lập"
        };
        notes_sub_dutchi = new String[]{
                "Đứt chì thân",
                "Đứt chì gốc",
                "Đứt chì khóa nước"
        };
        notes_sub_kinhdoanh = new String[]{
                "Quán ăn",
                "Nhà hàng",
                "Khách sạn",
                "Văn phòng công ty",
                "Cửa hàng",
                "Tạp hóa",
                "Sản xuất nước đá",
                "Sản xuất nước tinh khiết",
                "Bệnh viện",
                "Nhà trẻ",
                "Trường học tư thục",
                "Khác"
        };
    }

    public static final Note getInstance() {
        return instance;
    }

    public String[] getNotes() {
        return notes;

    }

    public String[] getNotes_sub_dutchi() {
        return notes_sub_dutchi;

    }

    public String[] getNotes_sub_kinhdoanh() {
        return notes_sub_kinhdoanh;

    }
}
