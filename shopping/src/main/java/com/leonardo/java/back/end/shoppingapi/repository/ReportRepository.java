package com.leonardo.java.back.end.shoppingapi.repository;

import com.leonardo.java.back.end.shoppingapi.model.Shop;
import com.leonardo.java.back.end.shoppingapi.model.dto.ShopReportDTO;

import java.util.Date;
import java.util.List;

public interface ReportRepository {

    public List<Shop> getShopByFilters(
            Date dataInicio,
            Date dataFim,
            Float valorMinimo
    );

    public ShopReportDTO getReportByDate(
        Date dataInicio,
        Date dataFim
    );
}
