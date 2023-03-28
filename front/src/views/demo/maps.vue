<template>
  <div ref="echarts" style="width:800px;height:600px" >
  </div>
</template>

<script>
import * as echarts from "echarts"
export default {
  name: 'maps',
  data(){
    return{
      options : {
      }
    }
  },
  methods:{
    initChart: function () {
      let data = require('/public/json/data.json')
      /*子组件中加了ref即可通过this.$refs.ref.method调用子组件的方法*/
      let map = echarts.init(this.$refs.echarts)
      let myJson = require('/public/json/hangzhou.json')
      echarts.registerMap('hangzhou', myJson)
      this.options = {
        geo: {
          map: 'hangzhou',
          //中心点(经纬度)`
          center: [112.298572, 30.584355],
          //缩放比例
          zoom: 1.2,
          label: {
            emphasis: {
              show: true,
              color: '#ffffff'
            }
          },
          aspectScale: 0.85,
          //是否允许拖拽
          roam: true,
          itemStyle: {
            normal: {
              areaColor: '#1B3B9F',
              borderColor: '#02e1fe',
              opcity: 0.8
            },
            emphasis: {
              areaColor: '#02e1fe'
            }
          }
        }
      }
      map.setOption(this.options);
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart();
    });
  }
}
</script>

<style scoped>

</style>
