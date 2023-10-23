package com.example.duancuahang.Class;

public class ThongTinCuaHang {
    private String soDienThoai, tenNguoiDangKyBanHang, tenCuaHang,
    diaChiCuaHang, emailCuaHang, maSoThue, cccdFront, cccdBack;

    @Override
    public String toString() {
        return "ThongTinCuaHang{" +
                "soDienThoai='" + soDienThoai + '\'' +
                ", tenNguoiDangKyBanHang='" + tenNguoiDangKyBanHang + '\'' +
                ", tenCuaHang='" + tenCuaHang + '\'' +
                ", diaChiCuaHang='" + diaChiCuaHang + '\'' +
                ", emailCuaHang='" + emailCuaHang + '\'' +
                ", maSoThue='" + maSoThue + '\'' +
                ", cccdFront='" + cccdFront + '\'' +
                ", cccdBack='" + cccdBack + '\'' +
                '}';
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getTenNguoiDangKyBanHang() {
        return tenNguoiDangKyBanHang;
    }

    public void setTenNguoiDangKyBanHang(String tenNguoiDangKyBanHang) {
        this.tenNguoiDangKyBanHang = tenNguoiDangKyBanHang;
    }

    public String getTenCuaHang() {
        return tenCuaHang;
    }

    public void setTenCuaHang(String tenCuaHang) {
        this.tenCuaHang = tenCuaHang;
    }

    public String getDiaChiCuaHang() {
        return diaChiCuaHang;
    }

    public void setDiaChiCuaHang(String diaChiCuaHang) {
        this.diaChiCuaHang = diaChiCuaHang;
    }

    public String getEmailCuaHang() {
        return emailCuaHang;
    }

    public void setEmailCuaHang(String emailCuaHang) {
        this.emailCuaHang = emailCuaHang;
    }

    public String getMaSoThue() {
        return maSoThue;
    }

    public void setMaSoThue(String maSoThue) {
        this.maSoThue = maSoThue;
    }

    public String getCccdFront() {
        return cccdFront;
    }

    public void setCccdFront(String cccdFront) {
        this.cccdFront = cccdFront;
    }

    public String getCccdBack() {
        return cccdBack;
    }

    public void setCccdBack(String cccdBack) {
        this.cccdBack = cccdBack;
    }

    public ThongTinCuaHang(String soDienThoai, String tenNguoiDangKyBanHang, String tenCuaHang, String diaChiCuaHang, String emailCuaHang, String maSoThue, String cccdFront, String cccdBack) {
        this.soDienThoai = soDienThoai;
        this.tenNguoiDangKyBanHang = tenNguoiDangKyBanHang;
        this.tenCuaHang = tenCuaHang;
        this.diaChiCuaHang = diaChiCuaHang;
        this.emailCuaHang = emailCuaHang;
        this.maSoThue = maSoThue;
        this.cccdFront = cccdFront;
        this.cccdBack = cccdBack;
    }

    public ThongTinCuaHang() {
    }
}
