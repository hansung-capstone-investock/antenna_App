package com.example.antenna.dataclass

data class sectorData(
        var per: perData? = null,
        var pbr: pbrData? = null,
        var psr: psrData? = null,
        var roa: roaData? = null,
        var roe: roeData? = null
)