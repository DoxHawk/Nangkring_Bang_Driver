package com.example.nangkringbangdriver.Model;

import com.google.firebase.Timestamp;

public class Model_Orders_Driver {

    private String pesanan_bukti, pesanan_catatan, pesanan_metode, pesanan_status, pesanan_alamat;
    private int pesanan_sub;
    private Timestamp pesanan_tgl_order, pesanan_tgl_bayar;

    public Model_Orders_Driver() {
    }

    public Model_Orders_Driver(String pesanan_bukti, String pesanan_catatan, String pesanan_metode, String pesanan_status, String pesanan_alamat, int pesanan_sub, Timestamp pesanan_tgl_order, Timestamp pesanan_tgl_bayar) {
        this.pesanan_bukti = pesanan_bukti;
        this.pesanan_catatan = pesanan_catatan;
        this.pesanan_metode = pesanan_metode;
        this.pesanan_status = pesanan_status;
        this.pesanan_alamat = pesanan_alamat;
        this.pesanan_sub = pesanan_sub;
        this.pesanan_tgl_order = pesanan_tgl_order;
        this.pesanan_tgl_bayar = pesanan_tgl_bayar;
    }

    public String getPesanan_bukti() {
        return pesanan_bukti;
    }

    public void setPesanan_bukti(String pesanan_bukti) {
        this.pesanan_bukti = pesanan_bukti;
    }

    public String getPesanan_catatan() {
        return pesanan_catatan;
    }

    public void setPesanan_catatan(String pesanan_catatan) {
        this.pesanan_catatan = pesanan_catatan;
    }

    public String getPesanan_metode() {
        return pesanan_metode;
    }

    public void setPesanan_metode(String pesanan_metode) {
        this.pesanan_metode = pesanan_metode;
    }

    public String getPesanan_status() {
        return pesanan_status;
    }

    public void setPesanan_status(String pesanan_status) {
        this.pesanan_status = pesanan_status;
    }

    public String getPesanan_alamat() {
        return pesanan_alamat;
    }

    public void setPesanan_alamat(String pesanan_alamat) {
        this.pesanan_alamat = pesanan_alamat;
    }

    public int getPesanan_sub() {
        return pesanan_sub;
    }

    public void setPesanan_sub(int pesanan_sub) {
        this.pesanan_sub = pesanan_sub;
    }

    public Timestamp getPesanan_tgl_order() {
        return pesanan_tgl_order;
    }

    public void setPesanan_tgl_order(Timestamp pesanan_tgl_order) {
        this.pesanan_tgl_order = pesanan_tgl_order;
    }

    public Timestamp getPesanan_tgl_bayar() {
        return pesanan_tgl_bayar;
    }

    public void setPesanan_tgl_bayar(Timestamp pesanan_tgl_bayar) {
        this.pesanan_tgl_bayar = pesanan_tgl_bayar;
    }
}
