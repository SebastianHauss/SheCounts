const users = [
    {
        username: "lena.keller",
        created: "05.01.2024",
        email: "lena.keller@web.de",
        country: "DE",
        status: "active",
        avatar: "https://randomuser.me/api/portraits/women/21.jpg"
    },
    {
        username: "tobias.schneider",
        created: "11.03.2023",
        email: "t.schneider@gmx.at",
        country: "AT",
        status: "inactive",
        avatar: "https://randomuser.me/api/portraits/men/15.jpg"
    },
    {
        username: "laura.moser",
        created: "22.07.2022",
        email: "laura.moser@gmail.com",
        country: "CH",
        status: "active",
        avatar: "https://randomuser.me/api/portraits/women/47.jpg"
    },
    {
        username: "marco.fischer",
        created: "03.11.2023",
        email: "marco.fischer@hotmail.com",
        country: "DE",
        status: "active",
        avatar: "https://randomuser.me/api/portraits/men/33.jpg"
    },
    {
        username: "julia.meier",
        created: "14.02.2024",
        email: "julia.meier@outlook.ch",
        country: "CH",
        status: "inactive",
        avatar: "https://randomuser.me/api/portraits/women/12.jpg"
    },
    {
        username: "daniel.huber",
        created: "30.08.2022",
        email: "d.huber@aon.at",
        country: "AT",
        status: "active",
        avatar: "https://randomuser.me/api/portraits/men/18.jpg"
    },
    {
        username: "sophie.wagner",
        created: "09.12.2023",
        email: "sophie.wagner@t-online.de",
        country: "DE",
        status: "active",
        avatar: "https://randomuser.me/api/portraits/women/8.jpg"
    },
    {
        username: "lukas.baumann",
        created: "18.06.2023",
        email: "lukas.baumann@bluewin.ch",
        country: "CH",
        status: "inactive",
        avatar: "https://randomuser.me/api/portraits/men/27.jpg"
    },
    {
        username: "anna.gruber",
        created: "27.09.2023",
        email: "anna.gruber@gmail.com",
        country: "AT",
        status: "active",
        avatar: "https://randomuser.me/api/portraits/women/36.jpg"
    },
    {
        username: "philipp.neumann",
        created: "01.04.2024",
        email: "philipp.neumann@gmx.de",
        country: "DE",
        status: "active",
        avatar: "https://randomuser.me/api/portraits/men/44.jpg"
    }
];

/* Status Badge (active / inactive) je nach Status */
const statusBadge = status =>
    `<span class="badge ${status === "active" ? "bg-success" : "bg-secondary"}">${status}</span>`;

/* Füllt Users mit Avatar von der Liste in den Userlist-Table */
$(function () {
    users.forEach(user => {
        $("#user-table-body").append(`
      <tr>
        <td>
          <img src="${user.avatar}" alt="${user.username}" class="rounded-circle me-2" width="32" height="32">
          <a href="#">${user.username}</a>
        </td>
        <td>${user.created}</td>
        <td>${user.email}</td>
        <td>${user.country}</td>
        <td>${statusBadge(user.status)}</td>
      </tr>
    `);
    });
});

/* Search and Sort JS */