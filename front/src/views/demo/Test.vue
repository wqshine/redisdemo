<template>
  <div ref="Test">
    <slot name="slot1">
    </slot>
    <p>{{message}}</p>
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: "Test",
  data(){
    return {
      message:'通用组件',
      data:'',
      test:'测试'
    }
  }

}
</script>

<style scoped>

</style>
