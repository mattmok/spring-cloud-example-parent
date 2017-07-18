define(['async!http://api.map.baidu.com/api?v=2.0&ak=DwFAUTwtxbHyh0POXUr1deglTP0vTMEZ', 'resources/components/js/GPSUtils.js'], function(){
	var dependencies = [
	   'http://api.map.baidu.com/library/LuShu/1.2/src/LuShu_min.js',
	   'http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.js',
	   'resources/components/js/maplib.js',
	   'resources/components/js/textIconOverlay.js',
	   'css!http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.css'];
	require(dependencies, function () {
		BMapLib.HistoryTrackPlayer = function(player, points){
			return new BMapLib.LuShu(player, points, {
				icon : new BMap.Icon('resources/images/monitor/map/marker4.png', new BMap.Size(64,64)),
				autoView:true,
				speed: 200,
				enableRotation:true,
				landmarkPois: []
			});
		};
		BMapLib.LuShu.prototype.isPaused = function(){
			return this._fromPause;
		};
		BMapLib.LuShu.prototype.isStoped = function(){
			return this._fromStop;
		};
		BMapLib.LuShu.prototype.getStatus = function(){
			return this._fromPause || this._fromStop;
		};
	});
	
	BMap.PointVector = function(vector){
		var icons = [
		  "resources/images/monitor/map/offline-lg.png",
		  "resources/images/monitor/map/online-lg.png",
		  "resources/images/monitor/map/lowpower-lg.png",
		  "resources/images/monitor/map/using-lg.png",
		  "resources/images/vehicle.png"
		];
		
		var bd09 = GPSUtils.fromWGS84ToBD09(vector.center.lat, vector.center.lng);
		var icon, marker, point = new BMap.Point(bd09.lng, bd09.lat);
		if (vector.info.state.index == 2) {
			icon = new BMap.Icon(icons[vector.info.state.index], new BMap.Size(64,64));
		}else if (vector.info.runState.index == 2) {
			icon = new BMap.Icon(icons[3], new BMap.Size(64,64));
		} else {
			icon = new BMap.Icon(icons[vector.info.state.index], new BMap.Size(64,64));
		}
		var marker = new BMap.Marker(point,{"icon":icon, "title": vector.name});
		marker.id = vector.id;
		marker.data = vector;
		return marker;
	};
	BMap.Map.prototype.addVector = function(node){
		var map = this, vector = new BMap.PointVector(node);
		map.addOverlay(vector);
		return vector;
	};
	BMap.Map.prototype.resetVector = function(node){
		var overlays = this.getOverlays();
		var vectors = overlays.map(function(e){return e.id});
		var i = vectors.indexOf(node.id);
		if(i !== -1 && overlays[i] !== undefined){
			var bd09 = GPSUtils.fromWGS84ToBD09(node.center.lat, node.center.lng);
			overlays[i].setPosition(new BMap.Point(bd09.lng, bd09.lat));
		}
	};
	BMap.Map.prototype.removeVector = function(nodes){
		if(!Array.isArray(nodes)){
			nodes = [nodes];
		}
		var overlays = this.getOverlays();
		var vectors = overlays.map(function(e){return e.id});
		for(var i = 0; i < nodes.length; i++){
			if(nodes[i] !== undefined && nodes[i].id !== undefined){
				var k = vectors.indexOf(nodes[i].id);
				this.removeOverlay(overlays[k]);
			}
		}
	};
	
	/**
	 * @constructor
	 * @param data {json Object}
	 * 	{
	 *		"id": "20160900",
	 *		"paths": [
	 * 			{lat: 34.758123, lng: 113.644865},
	 *		 	{lat: 34.757870, lng: 113.648907},
	 *			{lat: 34.756061, lng: 113.648728},
	 *			{lat: 34.756002, lng: 113.651027},
	 *			{lat: 34.756521, lng: 113.651027},
	 *			{lat: 34.756432, lng: 113.654153},
	 *		]
	 *  }
	 * @style 
	 *  {
	 *	     strokeColor:"red",     //边线颜色。
	 *       fillColor:"red",       //填充颜色。当参数为空时，圆形将没有填充效果。
	 *	     strokeWeight: 3,       //边线的宽度，以像素为单位。
	 *	     strokeOpacity: 0.8,	   //边线透明度，取值范围0 - 1。
	 *	     fillOpacity: 0.6,      //填充的透明度，取值范围0 - 1。
	 *	     strokeStyle: 'solid',   //边线的样式，solid或dashed。
	 *	     enableEditing: false,
	 *	     enableClicking: true
	 *	}
	 */
	BMap.Station = function(data, style){
		var ploygon = new BMap.Polygon((function(array){
			var points = [];
			angular.forEach(array, function(item) {
				var bd09 = GPSUtils.fromWGS84ToBD09(item.lat, item.lng);
				this.push(new BMap.Point(bd09.lng, bd09.lat));
			}, points);
			return points;
		})(data.paths), style);
		ploygon.id = data.id;
		return ploygon;
	};
	
	BMap.Area = function(data, style){
		var bd09 = GPSUtils.fromWGS84ToBD09(data.lat, data.lng);
		var point = new BMap.Point(bd09.lng, bd09.lat);
		var ploygon = new BMap.Circle(point, data.radius, style);
		ploygon.id = data.id;
		return ploygon;
	};
	
	/**
	 * @constructor
	 * @param data {json Object}
	 * 	{
	 *		"id": "20160900",
	 *		"paths": [
	 *			{lat: 34.759368, lng: 113.643446},
	 *			{lat: 34.757478, lng: 113.643284},
	 *			{lat: 34.757396, lng: 113.644766},
	 *			{lat: 34.759272, lng: 113.644964},
	 *		]
	 *  }
	 * @style 
	 *  {
	 *	     strokeColor:"red",     //边线颜色。
	 *       fillColor:"red",       //填充颜色。当参数为空时，圆形将没有填充效果。
	 *	     strokeWeight: 3,       //边线的宽度，以像素为单位。
	 *	     strokeOpacity: 0.8,	   //边线透明度，取值范围0 - 1。
	 *	     fillOpacity: 0.6,      //填充的透明度，取值范围0 - 1。
	 *	     strokeStyle: 'solid',   //边线的样式，solid或dashed。
	 *	     enableEditing: false,
	 *	     enableClicking: true
	 *	}
	 */
	BMap.RouterLine = function(data, style){
		var polyline = new BMap.Polyline((function(array){
			var points = [];
			angular.forEach(array, function(item) {
				var bd09 = GPSUtils.fromWGS84ToBD09(item.lat, item.lng);
				this.push(new BMap.Point(bd09.lng, bd09.lat));
			}, points);
			return points;
		})(data.paths), style);
		polyline.id = data.id;
		return polyline;
	};
	
	/**
	 * 删除地图上的场区覆盖物
	 * @param item {json Object} 要删除的对象，id不能为undefined
	 */
	BMap.Map.prototype.removeStation = function(station){
		var overlays = this.getOverlays();
		var vectors = overlays.map(function(e){return e.id});
		var i = vectors.indexOf(station.id);
		if(i !== -1){
			this.removeOverlay(overlays[i]);
		}
	};
	
	
	BMap.Map.prototype.removeLine = function(nodes){
		if(!Array.isArray(nodes)){
			nodes = [nodes];
		}
		var overlays = this.getOverlays();
		var vectors = overlays.map(function(e){return e.id});
		for(var i = 0; i < nodes.length; i++){
			if(nodes[i] !== undefined && nodes[i].id !== undefined){
				var k = vectors.indexOf(nodes[i].id);
				this.removeOverlay(overlays[k]);
			}
		}
	};
	
	BMap.PathsFromBD09ToWGS84 = function(data){
		var paths = [];
		angular.forEach(data, function(item) {
			var wgs84 = GPSUtils.fromBD09ToWGS84(item.lat, item.lng);
			this.push(wgs84);
		}, paths);
		return paths;
	};
	
	BMap.PathFromBD09ToWGS84 = function(data){
		var wgs84 = GPSUtils.fromBD09ToWGS84(data.lat, data.lng);
		return wgs84;
	};
	
	BMap.MarketPoint = function(data){
		var bd09 = GPSUtils.fromWGS84ToBD09(data.lat, data.lng);
		var market = new BMap.Marker(bd09);
		return market;
	};
	
	return BMap;
});