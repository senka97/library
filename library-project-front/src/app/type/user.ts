export interface User {

  username: string;
  role: "ROLE_USER" | "ROLE_ADMIN";
  firstName: string;
  lastName: string;

}