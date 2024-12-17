/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 3.2.1263 on 2024-12-17 23:33:56.

export interface AdminQueryResp {
  parameters: ConfigurationValue[];
}

export interface ConfigurationValue {
  name: string;
  value: string;
  writeable: boolean;
}

export interface AdminUpdateReq {
  values: UpdateValue[];
}

export interface UpdateValue {
  name: string;
  value: string;
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
