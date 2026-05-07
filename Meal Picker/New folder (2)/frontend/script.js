let lastMeal = null;

let pickCount = Number(localStorage.getItem("pickCount")) || 0;
let ratingCount = Number(localStorage.getItem("ratingCount")) || 0;

let categoryStats = JSON.parse(localStorage.getItem("categoryStats")) || {
    breakfast: 0,
    lunch: 0,
    snack: 0
};

window.onload = function () {
    loadFavorites();
    updateStats();
    showMealOfDay();
};

function getMeal() {
    let category = document.getElementById("category").value;
    const vegetarian = document.getElementById("vegetarian").checked;
    const maxTime = document.getElementById("maxTime").value || 999;
    const maxCalories = document.getElementById("maxCalories").value || 9999;

    if (category === "all") {
        category = getSmartCategory();
    }

    showToast("Thinking...");

    fetch("http://localhost:8080/meal?type=" + category +
        "&vegetarian=" + vegetarian +
        "&maxTime=" + maxTime +
        "&maxCalories=" + maxCalories)
        .then(response => response.json())
        .then(data => {
            setTimeout(() => {
                lastMeal = data;
                showMeal(data);

                if (data.name !== "No meal found") {
                    addHistory(data);
                    pickCount++;
                    localStorage.setItem("pickCount", pickCount);

                    if (categoryStats[data.category] !== undefined) {
                        categoryStats[data.category]++;
                        localStorage.setItem("categoryStats", JSON.stringify(categoryStats));
                    }
                }

                updateStats();
                showToast("Meal ready 🍽️");
            }, 650);
        })
        .catch(() => {
            showToast("Backend is not running ❌");
        });
}

function showMeal(meal) {
    document.getElementById("mealCard").classList.remove("hidden");
    document.getElementById("mealEmoji").innerText = meal.emoji;
    document.getElementById("mealName").innerText = meal.name;

    document.getElementById("mealInfo").innerText =
        meal.category.toUpperCase() +
        " • " + meal.time + " min" +
        " • " + meal.difficulty +
        " • " + meal.calories + " kcal";

    document.getElementById("mealNote").innerText = meal.note;
}

function surpriseMe() {
    const categories = ["breakfast", "lunch", "snack"];
    const times = ["5", "10", "15", "999"];
    const calories = ["200", "350", "500", "9999"];

    document.getElementById("category").value =
        categories[Math.floor(Math.random() * categories.length)];

    document.getElementById("maxTime").value =
        times[Math.floor(Math.random() * times.length)];

    document.getElementById("maxCalories").value =
        calories[Math.floor(Math.random() * calories.length)];

    document.getElementById("vegetarian").checked = Math.random() > 0.5;

    getMeal();
}

function getSmartCategory() {
    let max = Math.max(categoryStats.breakfast, categoryStats.lunch, categoryStats.snack);

    if (max === 0) {
        const categories = ["breakfast", "lunch", "snack"];
        return categories[Math.floor(Math.random() * categories.length)];
    }

    if (categoryStats.breakfast === max) return "breakfast";
    if (categoryStats.lunch === max) return "lunch";
    return "snack";
}

function addHistory(meal) {
    const history = document.getElementById("history");

    const li = document.createElement("li");
    li.innerText = meal.emoji + " " + meal.name + " (" + meal.calories + " kcal)";
    history.prepend(li);

    if (history.children.length > 8) {
        history.removeChild(history.lastChild);
    }
}

function saveFavorite() {
    if (!lastMeal || lastMeal.name === "No meal found") return;

    let favorites = JSON.parse(localStorage.getItem("favorites")) || [];

    let exists = favorites.some(meal => meal.name === lastMeal.name);

    if (!exists) {
        favorites.push(lastMeal);
        localStorage.setItem("favorites", JSON.stringify(favorites));
        showToast("Saved to favorites ❤️");
    } else {
        showToast("Already in favorites");
    }

    loadFavorites();
    updateStats();
}

function loadFavorites() {
    const list = document.getElementById("favorites");
    list.innerHTML = "";

    let favorites = JSON.parse(localStorage.getItem("favorites")) || [];

    favorites.forEach(meal => {
        const li = document.createElement("li");
        li.innerText = meal.emoji + " " + meal.name + " (" + meal.calories + " kcal)";
        list.appendChild(li);
    });
}

function clearHistory() {
    document.getElementById("history").innerHTML = "";
    updateStats();
    showToast("History cleared");
}

function clearFavorites() {
    localStorage.removeItem("favorites");
    loadFavorites();
    updateStats();
    showToast("Favorites cleared");
}

function copyMeal() {
    if (!lastMeal || lastMeal.name === "No meal found") return;

    const text = "I got " + lastMeal.name + " " + lastMeal.emoji +
        " from Lazy Meal Picker. It has around " + lastMeal.calories + " kcal.";

    navigator.clipboard.writeText(text);
    showToast("Copied 📋");
}

function rateMeal(stars) {
    if (!lastMeal || lastMeal.name === "No meal found") return;

    ratingCount++;
    localStorage.setItem("ratingCount", ratingCount);

    showToast("You rated it " + stars + "⭐");
    updateStats();
}

function showMealOfDay() {
    const today = new Date().toDateString();
    const saved = localStorage.getItem("dailyMeal");

    if (saved) {
        const parsed = JSON.parse(saved);

        if (parsed.date === today) {
            document.getElementById("dailyMeal").innerText =
                parsed.meal.emoji + " " + parsed.meal.name + " • " + parsed.meal.calories + " kcal";
            return;
        }
    }

    fetch("http://localhost:8080/meal?type=all")
        .then(response => response.json())
        .then(data => {
            localStorage.setItem("dailyMeal", JSON.stringify({
                date: today,
                meal: data
            }));

            document.getElementById("dailyMeal").innerText =
                data.emoji + " " + data.name + " • " + data.calories + " kcal";
        })
        .catch(() => {
            document.getElementById("dailyMeal").innerText = "Start backend to load";
        });
}

function toggleTheme() {
    document.body.classList.toggle("dark");
}

function updateStats() {
    document.getElementById("pickCount").innerText = pickCount;

    document.getElementById("favCount").innerText =
        (JSON.parse(localStorage.getItem("favorites")) || []).length;

    document.getElementById("historyCount").innerText =
        document.getElementById("history").children.length;

    document.getElementById("ratingCount").innerText = ratingCount;
}

function showToast(text) {
    let oldToast = document.querySelector(".toast");
    if (oldToast) oldToast.remove();

    let toast = document.createElement("div");
    toast.className = "toast";
    toast.innerText = text;

    document.body.appendChild(toast);

    setTimeout(() => {
        toast.remove();
    }, 2000);
}