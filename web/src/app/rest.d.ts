/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 3.2.1263 on 2024-11-29 22:10:15.

export interface AdminQueryResp {
  parameters: { [index: string]: string };
}

export interface HomeResp {
  displayName: string;
  menuItems: MenuItem[];
  loggedInUser: User;
}

export interface LoginReq {
  email: string;
}

export interface LoginResp {
  token: string;
}

export interface MenuItem extends Comparable<MenuItem> {
  name: string;
  link: string;
  order: number;
}

export interface PageResp {
  pageContent: string;
  title: string;
}

export interface User {
  id: number;
  fullName: string;
  preferredName: string;
  email: string;
}

export interface Comparable<T> {
}
