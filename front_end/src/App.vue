<template>
  <v-app id="inspire">

    <NavBar />
    <router-view />
    
  </v-app>
</template>

<script>
import NavBar from "./components/NavBar.vue"
import SideBar from "./components/SideBar.vue"

export default {
  name: "App",

    components: {
      NavBar,
      SideBar
    },

  created() {
    console.log("created()");
  },
 
  // updated(){
  //   var div = document.getElementById("compiledMarkdown");
  //   if(this.$vuetify.theme.dark == true)
  //     div.style.color = "white";
  //   else
  //     div.style.color = "black";
  // },
};
</script>

<style scoped>
html, body {
  margin: 0;
  height: 100%;
  font-family: 'Helvetica Neue', Arial, sans-serif;
  color: #333;
}
.v-list {
  padding: 0;
}
.sidesubheader {
    border-bottom: solid 3px rgb(100, 93, 102);
    display: inline-block;
    margin: 2.5em 0 0 0;
    padding: 0 0.75em 0.5em 0;
}
/* .theme--light.v-list-item:not(.v-list-item--active) {
    color: #615f75 !important;
    font-size:0.5em;
}
.theme--light.v-list-item:not(.v-list-item--active):not(.v-list-item--disabled):hover {
    color: rgb(214, 198, 219) !important;
} */
.v-list-item {
    margin: 0.5em 2em 0;
    padding: 1em 0;
    height: 1em;
}
.v-list-item:not(:last-child) {
    font-family: 'Roboto Slab', sans-serif;
    border-bottom: solid 1px rgba(210, 215, 217, 0.75);
}
.v-subheader:not(.sidesubheader) {
    font-size: 1.25em;
    color: rgb(95, 90, 97) !important;
    border-bottom: solid 2px rgb(83, 81, 83);
    display: inline-block;
    margin: 0.5em;
    padding: 0.75em 0;
}
</style>
