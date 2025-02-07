  //clear The History state to prevent navigating back to the Protected pages after logout
  window.history.pushState(null,null,window.location.href);
  window.addEventListener('popstate' ,function(event)
  {
      window.history.pushState(null,null,window.location.href);
  });
  let secondsLeft = [[${secondsLeft}]];
  const timerElement = document.getElementById('timer');
  function updateTimer() {
      timerElement.textContent=`Redirecting To login page in ${secondsLeft} seconds....`;
      if(secondsLeft <= 0){
          window.location.href='/app/login';
      }else{
          secondsLeft--;
          setTimeout(updateTimer, 2000);
      }
  }
  updateTimer();