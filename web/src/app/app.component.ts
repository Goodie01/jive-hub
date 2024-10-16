import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {ApiService} from "./api.service";
import {Title} from "@angular/platform-browser";
import {unified} from 'unified'
import rehypeStringify from 'rehype-stringify'
import remarkFrontmatter from 'remark-frontmatter'
import remarkGfm from 'remark-gfm'
import remarkParse from 'remark-parse'
import remarkRehype from 'remark-rehype'

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  constructor(private apiService: ApiService, private titleService:Title) {
    apiService.pageResponse().subscribe(value => {

      unified()
          .use(remarkParse)
          .use(remarkFrontmatter)
          .use(remarkGfm)
          .use(remarkRehype)
          .use(rehypeStringify)
          .process(value.pageContent)
          .then(value => {
            this.content = String(value)
          })

      titleService.setTitle(value.title)
    })
  }

  title = 'web';
  content: string = '';
}
