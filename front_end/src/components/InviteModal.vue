<template>
<v-dialog v-model="$store.state.isInviteModal" max-width="500px">
  <v-card 
    class="mx-auto"
  >
    <v-toolbar
      color="rgb(255, 179, 102)"
      dark
    >
      <v-text-field
        v-model="search"
        append-icon="mdi-plus"
        label="Search Email"
        single-line
        hide-details
      ></v-text-field>
    </v-toolbar>
    <v-list>
      <v-list-item
        v-for="item in $store.state.workspaceMemberList"
        :key="item.no"
      >
        <v-list-item-icon>
          <v-icon v-if="!(item.status)" color="pink">mdi-star</v-icon>
        </v-list-item-icon>

        <v-list-item-content>
          <v-list-item-title v-text="item.name"></v-list-item-title>
        </v-list-item-content>

        <v-list-item-group v-model="isOwner">
        <v-chip small color="orange" rounded filter dark v-if="item.status===2" style="margin-right: 1em">수락 대기 중</v-chip>

        <v-btn small color="orange" rounded outlined v-if="item.status!=0">내보내기</v-btn>
      </v-list-item-group>
      </v-list-item>
    </v-list>
  </v-card>
</v-dialog>
</template>

<script>
export default {
    name: 'InviteModal',
    data () {
      return {
        search: '',
        isOwner: false,
      }
    },
    mounted() {
      if(this.$store.state.isInviteModal){
        if(this.$store.state.workspaceMemberList.find(element => element.no === this.$store.state.userInfo.no).status === 0){
          isOwner = true;
        }
      }
    },
    methods: {
      
    },
}
</script>