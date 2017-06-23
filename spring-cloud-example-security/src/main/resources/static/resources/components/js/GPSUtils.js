var GPSUtils = {
    PI : 3.14159265358979324,
    x_pi : 3.14159265358979324 * 3000.0 / 180.0,
    //WGS-84 to GCJ-02
    fromWGS84ToGCJ02 : function (lat, lng) {
        if (this._outOfChina(lat, lng)){
        	return {'lat': lat, 'lng': lng};
        }
        var d = this._delta(lat, lng);
        return {'lat' : lat + d.lat,'lng' : lng + d.lng};
    },
	// WGS-84 to BD-09
	fromWGS84ToBD09 : function(lat, lng) {
		var gcj02 = this.fromWGS84ToGCJ02(lat, lng);
		return this.fromGCJ02ToBD09(gcj02.lat, gcj02.lng);
	},
    // WGS-84 to Web mercator
    //mercatorLat -> y mercatorLon -> x
	fromWGS84ToMercator : function(lat, lng) {
        var x = lng * 20037508.34 / 180.;
        var y = Math.log(Math.tan((90. + lat) * this.PI / 360.)) / (this.PI / 180.);
        y = y * 20037508.34 / 180.;
        return {'lat' : y, 'lng' : x};
    },
	// GCJ-02 to WGS-84
	fromGCJ02ToWGS84 : function(lat, lng) {
		if (this._outOfChina(lat, lng)) {
			return {'lat' : lat, 'lng' : lng };
		}
		var d = this._delta(lat, lng);
		return {'lat' : lat - d.lat, 'lng' : lng - d.lng };
	},
    //GCJ-02 to WGS-84 exactly
	fromGCJ02ToWGS84ForExactly : function (lat, lng) {
        var initDelta = 0.01;
        var threshold = 0.000000001;
        var dLat = initDelta, dLon = initDelta;
        var mLat = lat - dLat, mLon = lng - dLon;
        var pLat = lat + dLat, pLon = lng + dLon;
        var wgsLat, wgsLon, i = 0;
        while (1) {
            wgsLat = (mLat + pLat) / 2;
            wgsLon = (mLon + pLon) / 2;
            var tmp = this.gcj_encrypt(wgsLat, wgsLon)
            dLat = tmp.lat - lat;
            dLon = tmp.lon - lng;
            if ((Math.abs(dLat) < threshold) && (Math.abs(dLon) < threshold))
                break;
 
            if (dLat > 0) pLat = wgsLat; else mLat = wgsLat;
            if (dLon > 0) pLon = wgsLon; else mLon = wgsLon;
 
            if (++i > 10000) break;
        }
        return {'lat': wgsLat, 'lng': wgsLon};
    },
    //GCJ-02 to BD-09
	fromGCJ02ToBD09 : function(lat, lng) {
		var x = lng, y = lat;
		var z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * this.x_pi);
		var theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * this.x_pi);
		bdLon = z * Math.cos(theta) + 0.0065;
		bdLat = z * Math.sin(theta) + 0.006;
		return { 'lat' : bdLat, 'lng' : bdLon };
	},
	// BD-09 to GCJ-02
	fromBD09ToGCJ02 : function(lat, lng) {
		var x = lng - 0.0065, y = lat - 0.006;
		var z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * this.x_pi);
		var theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * this.x_pi);
		var gcjLon = z * Math.cos(theta);
		var gcjLat = z * Math.sin(theta);
		return { 'lat' : gcjLat, 'lng' : gcjLon };
	},
	// BD-09 to WGS-84
	fromBD09ToWGS84 : function(lat, lng) {
		var gcj02 = this.fromBD09ToGCJ02(lat, lng);
		return this.fromGCJ02ToWGS84(gcj02.lat, gcj02.lng);
	},
    // Web mercator to WGS-84
    // mercatorLat -> y mercatorLon -> x
    fromMercatorToWGS84 : function(lat, lng) {
        var x = lng / 20037508.34 * 180.;
        var y = lat / 20037508.34 * 180.;
        y = 180 / this.PI * (2 * Math.atan(Math.exp(y * this.PI / 180.)) - this.PI / 2);
        return {'lat' : y, 'lng' : x};
    },
    // two point's distance
    distance : function (latA, lonA, latB, lonB) {
        var earthR = 6371000.;
        var x = Math.cos(latA * this.PI / 180.) * Math.cos(latB * this.PI / 180.) * Math.cos((lonA - lonB) * this.PI / 180);
        var y = Math.sin(latA * this.PI / 180.) * Math.sin(latB * this.PI / 180.);
        var s = x + y;
        if (s > 1) s = 1;
        if (s < -1) s = -1;
        var alpha = Math.acos(s);
        var distance = alpha * earthR;
        return distance;
    },
    _delta : function (lat, lng) {
        var a = 6378245.0; //  a: 卫星椭球坐标投影到平面地图坐标系的投影因子。
        var ee = 0.00669342162296594323; //  ee: 椭球的偏心率。
        var dLat = this._transformLat(lng - 105.0, lat - 35.0);
        var dLon = this._transformLon(lng - 105.0, lat - 35.0);
        var radLat = lat / 180.0 * this.PI;
        var magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        var sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * this.PI);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * this.PI);
        return {'lat': dLat, 'lng': dLon};
    },
    _outOfChina : function (lat, lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    },
    _transformLat : function (x, y) {
        var ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * this.PI) + 20.0 * Math.sin(2.0 * x * this.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * this.PI) + 40.0 * Math.sin(y / 3.0 * this.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * this.PI) + 320 * Math.sin(y * this.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    },
    _transformLon : function (x, y) {
        var ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * this.PI) + 20.0 * Math.sin(2.0 * x * this.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * this.PI) + 40.0 * Math.sin(x / 3.0 * this.PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * this.PI) + 300.0 * Math.sin(x / 30.0 * this.PI)) * 2.0 / 3.0;
        return ret;
    }
};