$(function(){
    /*initializing jquery datatable to display weather data*/
    $("#weatherTable").DataTable({
    "searching": false,   // Search Box will Be Disabled
    "ordering": false,    // Ordering (Sorting on Each Column)will Be Disabled
    "info": false,         // Will show "1 to n of n entries" Text at bottom
    "lengthChange": false // Will Disabled Record number per page
    });
});

