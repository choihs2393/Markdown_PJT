<template>
<v-dialog v-model="$store.state.isInviteModal" max-width="500px">
  <v-card 
    class="mx-auto"
  >
    <v-toolbar
      color="indigo"
      dark
    >
      <v-text-field
        v-model="search"
<<<<<<< HEAD
        append-icon="mdi-magnify"
=======
        append-icon="mdi-plus"
>>>>>>> 1fa6931b47d6c9cc576611b8af6c8a1cc2047065
        label="Search"
        single-line
        hide-details
      ></v-text-field>
    </v-toolbar>
    <v-list>
      <v-list-item
        v-for="item in items"
        :key="item.title"
      >
        <v-list-item-icon>
          <v-icon v-if="item.icon" color="pink">mdi-star</v-icon>
        </v-list-item-icon>

        <v-list-item-content>
          <v-list-item-title v-text="item.title"></v-list-item-title>
        </v-list-item-content>

        <v-list-item-avatar>
          <v-img :src="item.avatar"></v-img>
        </v-list-item-avatar>
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
        items: [
          { icon: true, title: 'Jason Oner', avatar: 'https://cdn.vuetifyjs.com/images/lists/1.jpg' },
          { title: 'Travis Howard', avatar: 'https://cdn.vuetifyjs.com/images/lists/2.jpg' },
          { title: 'Ali Connors', avatar: 'https://cdn.vuetifyjs.com/images/lists/3.jpg' },
          { title: 'Cindy Baker', avatar: 'https://cdn.vuetifyjs.com/images/lists/4.jpg' },
        ],
      }
    },
}
</script>