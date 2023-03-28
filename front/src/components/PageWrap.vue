<template>
  <div class="fullList" ref="pageWrap">
  <!--  <div ref="pageTop">
      <slot name="pageTop"></slot>
    </div>-->
    <!--表格内容 或者其他-->
    <div class="pageContent" ref="pageContent">
      <slot v-bind:contentHeight="contentHeight"> </slot>
    </div>
  </div>
</template>

<script>
    var _ = require("lodash");
    export default {
      name: "PageWrap",
      data() {
        return {
          contentHeight: {},
        };
      },

      mounted() {
        this.handleResize();
        window.addEventListener("resize", this.handleResize);
      },
      destroyed() {
        // 全局监听事件在离开页面时要注销
        window.removeEventListener("resize", this.handleResize);
      },
      methods: {
        //内容高度
        handleResize: function () {
          let h1 = window.innerHeight;
          let h2 = this.$refs.pageContent.offsetTop;
          let contentHeight = h1 - h2 - 80;
          // console.log("contentHeight: ", contentHeight);

          this.contentHeight = contentHeight;
        },
      },
    };
</script>

<style scoped>
  .fullList {
    height: 100%;
    overflow: hidden;
  }
</style>
