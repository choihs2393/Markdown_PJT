<template>
  <v-app id="inspire">

    <NavBar />
    <SideBar v-if="!$store.state.isServerMode" />
    <SideBarShare v-if="$store.state.isServerMode" />
    <Home />
    <!-- <router-view /> -->
  </v-app>
</template>

<script>
import NavBar from "./components/NavBar.vue"
import SideBar from "./components/SideBar.vue"
import SideBarShare from "./components/SideBarShare.vue"
import Home from "./views/Home.vue"

export default {
  name: "App",

    components: {
      NavBar,
      SideBar,
      SideBarShare,
      Home,
    },

  mounted() {
    // console.log(localStorage.getItem('authorization'));
    if (localStorage.getItem('authorization')) {
      this.$store.dispatch('initUserInfo')
    }
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
</style>
