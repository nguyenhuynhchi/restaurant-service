// authService.js
let accessToken = localStorage.getItem("token") || null;
let refreshPromise = null;   // <— lưu Promise refresh khi đang thực hiện

export const getValidToken = async () => {
   // 1) Nếu đã có token và chưa cần refresh ⇒ trả luôn
   if (accessToken && await isTokenValid(accessToken)) {
      console.log("Không cần refresh token");
      return accessToken;
   }

   // 2) Nếu đã có một tiến trình refresh đang chạy ⇒ chờ nó
   if (refreshPromise) {
      return refreshPromise;
   }

   // 3) Bắt đầu refresh mới
   console.log("Đang Refresh token");
   refreshPromise = refreshToken().finally(() => {
      refreshPromise = null;         // reset khi xong
   });

   return refreshPromise;
};

const isTokenValid = async (token) => {
   try {
      const res = await fetch("http://localhost:8386/restaurant/auth/introspect", {
         method: "POST",
         headers: { "Content-Type": "application/json" },
         body: JSON.stringify({ token }),
      });
      const json = await res.json();
      console.log("Token hợp lệ")
      return json?.result?.valid;
   } catch {
      return false;
   }
};

const refreshToken = async () => {
   if (!accessToken) return null;           // không có token cũ

   const res = await fetch("http://localhost:8386/restaurant/auth/refresh", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ token: accessToken }),
   });
   const json = await res.json();

   if (!res.ok || json.code !== 1000) {
      accessToken = null;
      localStorage.removeItem("token");
      return null;
   }

   accessToken = json.result.token;
   localStorage.setItem("token", accessToken);
   return accessToken;
};
