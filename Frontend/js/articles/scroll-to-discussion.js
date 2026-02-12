console.log("Loaded scroll-to-discussion.js");

document.addEventListener('DOMContentLoaded', function () {
  const button = document.getElementById('floating-button');
  const target = document.getElementById('diskussion');

  // Exit early if elements don't exist on this page
  if (!button || !target) {
    console.log('Floating button or discussion section not found - skipping scroll handler');
    return;
  }

  console.log('Floating button scroll handler initialized');

  window.addEventListener('scroll', function () {
    const rect = target.getBoundingClientRect();

    if (rect.top < window.innerHeight) {
      button.style.opacity = '0';
      button.style.pointerEvents = 'none';
    } else {
      button.style.opacity = '1';
      button.style.pointerEvents = 'auto';
    }
  });
});