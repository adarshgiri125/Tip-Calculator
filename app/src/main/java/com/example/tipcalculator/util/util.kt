package com.example.tipcalculator.util

fun calculateTotalTip(TotalBill: Double , Tippercentage: Int): Double {
    return if(TotalBill > 1 && TotalBill.toString().isNotEmpty())
        (TotalBill * Tippercentage) / 100
    else 0.0
}

fun CalculateTotalBill(
    TotalBill: Double,
    splitBy: Int,
    Tippercentage: Int,

): Double{
    val Bill = calculateTotalTip(TotalBill = TotalBill,Tippercentage = Tippercentage) + TotalBill
     return(Bill/splitBy)
}
