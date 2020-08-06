export default {
    // URL: 'http://i3b104.p.ssafy.io:80/noteAPI',
    URL: 'http://localhost:8080/noteAPI',
    ROUTES: {
        signup: '/nonmember/signUp',
        login: '/login',
        email: '/nonmember/email',
        authSend: '/nonmember/email/authSend',
        authCheck: '/nonmember/email/authCheck',

        logout: '/account/logout',
    }
}