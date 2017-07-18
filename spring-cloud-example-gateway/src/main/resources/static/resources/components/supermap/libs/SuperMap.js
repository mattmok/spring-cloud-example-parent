define(['resources/components/supermap/libs/SuperMap-8.0.1-13403.js',
        'css!resources/components/supermap/theme/default/style.css',
		'css!resources/components/supermap/theme/default/google.css'
	], function() {
		var dependencies = [
		    'resources/components/supermap/libs/Lang/zh-CN.js'
    		//'resources/components/supermap/layer/EventPane.js',
    		//'resources/components/supermap/layer/FixedZoomLevels.js',
    		//'resources/components/supermap/layer/Google.js',
    		//'resources/components/supermap/layer/SphericalMercator.js'
    		];
		require(dependencies, function () {
			//require(['resources/components/supermap/layer/Google.v3.js']);
		});
		SuperMap.ImgPath = "resources/components/supermap/theme/images/";
		SuperMap.Layer.ClusterLayer.prototype.clusterStyles = [{
			"count" : 5,
			"style" : {
				fontColor : "#fff",
				graphic : true,
				externalGraphic : "resources/images/monitor/cluster/m2.png",
				graphicWidth : 53,
				graphicHeight : 53,
				labelXOffset : 0,
				labelYOffset : 0
			}
		},{
			"count" : 10,
			"style" : {
				fontColor : "#fff",
				graphic : true,
				externalGraphic : "resources/images/monitor/cluster/m2.png",
				graphicWidth : 65,
				graphicHeight : 65,
				labelXOffset : 0,
				labelYOffset : 0
			}
		},{
			"count" : "moreThanMax",
			"style" : {
				fontColor : "#fff",
				graphic : true,
				externalGraphic : "resources/images/monitor/cluster/m2.png",
				graphicWidth : 53,
				graphicHeight : 53,
				labelXOffset : 0,
				labelYOffset : 0
			}
		}];
		SuperMap.Feature.PointVector = function(x, y, info){
			var images = ['12daa9cc.png', '12daa9cd.png'];
			if(typeof x === 'object'){
				info = x.info;
				y = x.center.y;
				x = x.center.x;
			}
			var vector = new SuperMap.Feature.Vector(new SuperMap.Geometry.Point(x, y).transform("EPSG:4326","EPSG:3857"), {}, {
				fill:false,
				stroke:true,
				strokeColor:'#A29494',
				pointRadius: 8,
				graphic:true,
				externalGraphic:"resources/images/monitor/cluster/"+images[1],
//				externalGraphic:"resources/images/monitor/cluster/icons.png",
				graphicWidth:30,
				graphicHeight:42
//				graphicWidth:800,
//				graphicHeight:800,
//				graphicXOffset:-13,
//				graphicYOffset:-13
//				,
//				backgroundGraphic:"resources/images/monitor/cluster/icons.png",
//				backgroundWidth:800,
//				backgroundHeight:800,
//				backgroundXOffset:-13,
//				backgroundYOffset:-13
			});
			vector.info = info;
			return vector;
		};
		SuperMap.Feature.prototype.openInfoWindow = function(){
			this.layer.map.removeAllPopup();
            if(!this.isCluster){
            	var bounds = this.geometry.getBounds();
            	var center = bounds.getCenterLonLat();
            	var id = this.info && this.info.id ? this.info.id : "";
            	var name = this.info && this.info.name ? this.info.name:"";
            	var address = this.info && this.info.address ? this.info.address : "";
            	var totalNumBranch = this.info && this.info.totalNumBranch? this.info.totalNumBranch:"";
            	var idleNumBranch = this.info && this.info.idleNumBranch? this.info.idleNumBranch:"";
            	var image = this.info && this.info.image ? this.info.image :"http://pcsv0.map.bdimg.com/?qt=pr3d&panoid=0802890000141102001204472UZ&width=212&height=101&quality=80&fovx=180";
            	var contentHTML = "<div style='font-size:.8em; opacity: 0.8; overflow-y:hidden;'>";
            	contentHTML += "<div style='line-height:24px;'>"+ name +"</div>";
            	contentHTML += "<div><a href='javascript:openStation(\"" + id + "\");'><img width='212' height='100' src='" + image + "'></a></div>"
//            	contentHTML += "<div><a href='charging/station/index.html#2D481C4357D12F37E053C239A8C06E7D'><img width='212' height='100' src='" + image + "'></a></div>"
            	contentHTML += "<div style='line-height:24px;'>地址： <span>" + address + "</span></div></div>";
            	contentHTML += "<div style='line-height:24px;'>终端总数： <span>" + totalNumBranch + "</span></div>";
            	contentHTML += "<div style='line-height:24px;'>空闲终端数： <span>" + idleNumBranch + "</span></div></div>";
            	
            	
            	this.popup = new SuperMap.Popup.FramedCloud("popwin",
            			new SuperMap.LonLat(center.lon,center.lat),
            			null,
            			contentHTML,
            			null,
            			false,
            			null);
            	this.layer.map.addPopup(this.popup);
            	return this.popup;
            }
		}
		return SuperMap;
});