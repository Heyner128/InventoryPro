import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, ElementRef, Host, HostListener, signal, ViewChild } from '@angular/core';
import { PopoverComponent } from "../../popover/popover.component";

@Component({
  selector: 'app-search',
  imports: [PopoverComponent],
  animations: [trigger(
    'openClose', [
      state(
        'open',
        style({
          width: 'var(--fixed-size)'
        })
      ),
      state(
        'closed',
        style({
          position: 'absolute',
          width: '1px',
          height: '1px',
          margin: '-1px',
          padding: 0,
          overflow: 'hidden',
          clip: 'rect(0, 0, 0, 0)',
          border: 0,
          whiteSpace: 'nowrap'
        })
      ),
      transition('closed => open', [animate('0.2s')]),
    ]
  )],
  templateUrl: './search.component.html',
  styleUrl: './search.component.scss'
})
export class SearchComponent {
  searchOpen = signal(false);
  isInputFocused = signal(false);
  @ViewChild('input') searchInput: ElementRef<HTMLInputElement> | undefined;

  constructor(private readonly elementRef: ElementRef) {}

  focusInput() {
    if (this.searchInput) {
      this.searchInput.nativeElement.focus();
      this.isInputFocused.set(true);
    }
  }

  open() {
    this.searchOpen.set(true);
  }

  @HostListener('document:click', ['$event'])
  close(event: MouseEvent) {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.searchOpen.set(false);
      this.isInputFocused.set(false);
    }
  }
}
